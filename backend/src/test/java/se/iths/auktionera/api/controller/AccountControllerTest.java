package se.iths.auktionera.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import se.iths.auktionera.business.model.Account;
import se.iths.auktionera.business.model.Address;
import se.iths.auktionera.business.model.User;
import se.iths.auktionera.business.service.IAccountService;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user", roles = "USER")
class AccountControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private IAccountService accountService;

    @Test
    void getAccount(@Autowired MockMvc mvc) throws Exception {
        User user = User.builder().id(1).userName("testName").build();
        Address address = Address.builder().streetName("Gatan 2").city("Göteborg").postNr(12345).build();
        Account account = Account.builder().address(address).user(user).email("name@example.com").build();

        when(accountService.getAccount(any())).thenReturn(account);
        mvc.perform(get("/api/account").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'email':'name@example.com'}"))
                .andExpect(content().json("{'address':{'streetName':'Gatan 2'}}"))
                .andExpect(content().json("{'user':{'userName':'testName'}}"));

    }

    @Test
    void updateAccount(@Autowired MockMvc mvc) throws Exception {
        User user = User.builder().id(1).userName("NewName").build();
        Account account = Account.builder().user(user).build();

        Map<String, String> fields = Map.of("userName", user.getUserName());
        ObjectWriter objectWriter = mapper.writerFor(Map.class);
        String json = objectWriter.writeValueAsString(fields);

        when(accountService.updateAccount(any(), any())).thenReturn(account);
        mvc.perform(patch("/api/account").with(csrf()).characterEncoding("utf-8").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'user':{'userName':'NewName'}}"));

    }
}