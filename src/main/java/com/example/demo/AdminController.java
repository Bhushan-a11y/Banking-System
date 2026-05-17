package com.example.demo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.UserController.Userdetail;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@CrossOrigin(origins = "http://localhost:3000")

@RestController
public class AdminController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/admin/accounts/create-account")
public ResponseEntity<?>createAccount(@RequestBody Account account){
    boolean bool = accountService.createAccount(account);
    if(bool){
        return new ResponseEntity<>(account,HttpStatus.CREATED);
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
}

    @GetMapping("/admin/users/{accountNo}")
public ResponseEntity<?> getUser(@PathVariable Long accountNo) {
    
    System.out.println("--- SEARCHING FOR ACCOUNT NO: " + accountNo + " ---");
    Account account = accountRepository.findByAccno(accountNo).orElse(null);
    
    if (account != null) {
        System.out.println("SUCCESS: Found Account ID: " + account.getAccid());
        User user = userRepository.findByUserAccount(account);
        
        if (user != null) {
            System.out.println("SUCCESS: Found User: " + user.getUserName());
            Map<String, Object> reactData = new HashMap<>();
            reactData.put("userName", user.getUserName());
            
            String role = (user.getRoles() != null && !user.getRoles().isEmpty()) ? user.getRoles().get(0) : "USER";
            reactData.put("role", role);
            reactData.put("accountId", account.getAccid());
            reactData.put("balance", account.getBalance());
            
            return new ResponseEntity<>(reactData, HttpStatus.OK);
            
        } else {
            System.out.println("FAIL: Account exists, but NO User is linked to it in the database!");
        }
    } else {
        System.out.println("FAIL: Account " + accountNo + " does not exist in the database!");
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}
@GetMapping("/admin/accounts/get-all-accounts")
public ResponseEntity<?> getAllAccounts() {
    List<Account> accounts = accountService.getAllAccounts();

    if (accounts != null && !accounts.isEmpty()) {
        List<Map<String, Object>> formattedAccounts = new ArrayList<>();
        for (Account account : accounts) {
            Map<String, Object> accountData = new HashMap<>();
            accountData.put("accno", account.getAccno());
            accountData.put("balance", account.getBalance());
            accountData.put("accId", account.getAccid());
            accountData.put("createdAt", account.getCreatedAt()); 
            User user = userRepository.findByUserAccount(account);
            if (user != null){
                accountData.put("ownerName", user.getUserName());
            } else {
                accountData.put("ownerName", "Unknown");
            }

            formattedAccounts.add(accountData);
        }

        return new ResponseEntity<>(formattedAccounts, HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}
    
@PostMapping("/admin/users/create-a-user")
    public ResponseEntity<?> createUser(@RequestBody Userdetail innerUserController){
        User user = userService.createUser(innerUserController);
        if (user!=null) {
            return new ResponseEntity<>(user,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
     @GetMapping("/admin/get-all-users")
    public ResponseEntity<?> getAllUsers()
    {
        List<User> allusers = userService.getAllUsers();
        if(allusers!=null&&!allusers.isEmpty()){
            return new ResponseEntity<>(allusers,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Data@AllArgsConstructor@NoArgsConstructor
     public static class Admindetail{
        String adminName;
        String password;
        Long accountId;
    }

    @PostMapping("/admin/create-admin")
        public ResponseEntity<?> createAdmin(@RequestBody Admindetail user){
            User ADMIN = userService.createAdmin(user);
            if (ADMIN!=null) {
            return new ResponseEntity<>(ADMIN,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Userupdate{
            String userName;
            String password;
            List<String> roles = new ArrayList<>();
        }
        
        @PutMapping("/admin/users/update-an-user/{userId}")
        public ResponseEntity<?> updateUser(@PathVariable long userId,@RequestBody Userupdate userupdate ){
            User oldUser = userRepository.findById(userId).orElse(null);
            if (oldUser!=null) {
                oldUser.setUserName(userupdate.getUserName()!=null&&!userupdate.getUserName().equals("")?userupdate.getUserName():oldUser.getUserName());
                oldUser.setPassword(userupdate.getPassword()!=null&&!userupdate.getPassword().equals("")?userupdate.getPassword():oldUser.getPassword());
                if(!oldUser.getPassword().equals(userupdate.getPassword())){
                    userRepository.save(oldUser);
                }
                else{
                    userService.saveUpdatedUser(oldUser);
                }
                oldUser.setRoles(userupdate.getRoles()!=null&&!userupdate.getRoles().isEmpty()?userupdate.getRoles():oldUser.getRoles());
                userRepository.save(oldUser);
                return new ResponseEntity<>(oldUser,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        @GetMapping("/admin/transactions")
    public ResponseEntity<?> getAllBankTransactions() {
        // Grab literally every transaction in the database
        List<Transaction> allTransactions = transactionRepository.findAll();
        
        List<Map<String, Object>> responseList = new ArrayList<>();
        
        for (Transaction tx : allTransactions) {
            Map<String, Object> map = new HashMap<>();
            
            // Note: Use your exact variable names (like getTransId() instead of getId() if needed!)
            map.put("transactionId", tx.getTransactionId()); 
            map.put("senderAccount", tx.getSenderAccount() != null ? tx.getSenderAccount().getAccno() : "SYSTEM");
            map.put("receiverAccount", tx.getReceiverAccount() != null ? tx.getReceiverAccount().getAccno() : "SYSTEM");
            map.put("transactionType", tx.getTransactionType());
            map.put("date", tx.getMadeAT().toString());
            map.put("amount", tx.getTransAmount());
            
            responseList.add(map);
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
    }


