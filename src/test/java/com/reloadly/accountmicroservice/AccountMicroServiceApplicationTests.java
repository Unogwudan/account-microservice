package com.reloadly.accountmicroservice;

import com.reloadly.accountmicroservice.controllers.AccountControllerTest;
import com.reloadly.accountmicroservice.services.AccountServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountControllerTest.class,
        AccountServiceTest.class
})
public class AccountMicroServiceApplicationTests {}
