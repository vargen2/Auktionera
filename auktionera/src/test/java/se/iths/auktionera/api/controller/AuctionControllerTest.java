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
import se.iths.auktionera.business.model.Auction;
import se.iths.auktionera.business.model.CreateAuctionRequest;
import se.iths.auktionera.business.model.User;
import se.iths.auktionera.business.service.IAuctionService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user", roles = "USER")
class AuctionControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private IAuctionService auctionService;

    @Test
    void getAuctions(@Autowired MockMvc mvc) throws Exception {
        User seller = User.builder().id(1).userName("testName").build();
        Auction auction = Auction.builder().seller(seller).description("En stol").build();

        when(auctionService.getAuctions(any(), any())).thenReturn(List.of(auction));
        mvc.perform(get("/api/auctions").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{'description':'En stol'}]"))
                .andExpect(content().json("[{'seller':{'userName':'testName'}}]"));
    }

    @Test
    void getAuction(@Autowired MockMvc mvc) throws Exception {
        User seller = User.builder().id(1).userName("testName").build();
        Auction auction = Auction.builder().id(1000).seller(seller).description("En stol").build();

        when(auctionService.getAuction(1000)).thenReturn(auction);
        mvc.perform(get("/api/auctions/1000").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'description':'En stol'}"))
                .andExpect(content().json("{'id':1000}"))
                .andExpect(content().json("{'seller':{'userName':'testName'}}"));
    }

    @Test
    void createAuction(@Autowired MockMvc mvc) throws Exception {
        User seller = User.builder().id(1).userName("testName").build();
        Auction auction = Auction.builder().id(1000).seller(seller).description("En bra stol").build();

        CreateAuctionRequest en_bra_stol = CreateAuctionRequest.builder().title("Stol").description("En bra stol").build();
        ObjectWriter objectWriter = mapper.writerFor(CreateAuctionRequest.class);
        String json = objectWriter.writeValueAsString(en_bra_stol);

        when(auctionService.createAuction(anyString(), any())).thenReturn(auction);
        mvc.perform(post("/api/auctions").with(csrf()).characterEncoding("utf-8").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'seller':{'userName':'testName'}}"))
                .andExpect(content().json("{'description':'En bra stol'}"));

    }
}