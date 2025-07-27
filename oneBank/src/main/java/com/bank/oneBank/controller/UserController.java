package com.bank.oneBank.controller;

import com.bank.oneBank.dto.BankResponse;
import com.bank.oneBank.dto.EnquiryRequest;
import com.bank.oneBank.dto.UserDetailsDto;
import com.bank.oneBank.dto.UserRequest;
import com.bank.oneBank.exception.BusinessExecption;
import com.bank.oneBank.service.UserService;
import com.bank.oneBank.service.impl.GitHubService;
import com.bank.oneBank.service.impl.OpenAiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

@RestController
@RequestMapping("api/user")
@Tag(name = "User Account Management APIs")
public class UserController {

    @Autowired
    private UserService  userService;

    @Autowired
    private OpenAiService openAiService;

    @Autowired
    private GitHubService gitHubService;

    @Operation(
            summary = "Create New User Account",
            description = "Creating new bank account for a user and assigned with new account number.Email will be sent to the user to update on account creation status."
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 Created"
    )
    @PostMapping("/createAccount")
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "Check the account balance using the account number assigned to the user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Available Balance",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BankResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Error.class))
            )
    })
    @PostMapping("/balanceEnquiry")
    public ResponseEntity<BankResponse> balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) throws Exception, BusinessExecption {
        BankResponse response = null;
        try {
            response = userService.balanceEnquiry(enquiryRequest);
        }catch(RuntimeException e){
          throw new BusinessExecption(UUID.randomUUID().toString(),"Something went wrong while fetching the account balance",e);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @Operation(
            summary = "Name Enquiry",
            description = "Check the account holder name using the account number assigned to the user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 Success"
    )
    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.nameEnquiry(enquiryRequest);
    }

    @GetMapping("/userDetails")
    public UserDetailsDto userDetails(@RequestParam String accountNumber){
        return userService.userDetailsRequest(accountNumber);
    }

    @PostMapping("/suggestion")
    public String getCodeSuggestions(@RequestBody String prompt){
        return openAiService.getCodeSuggestions(prompt);
    }

    @GetMapping("/{owner}/{repo}/{pullNumber}/suggestion")
    public ResponseEntity<String> getGithubComments(@PathVariable String owner,
                                                    @PathVariable String repo,
                                                    @PathVariable String pullNumber){
        String response= null;
        String reviewComments = gitHubService.listCommentsForPullRequest(owner,repo,pullNumber);
        response= openAiService.getCodeSuggestions(reviewComments);
        return ResponseEntity.ok(response);
    }


}
