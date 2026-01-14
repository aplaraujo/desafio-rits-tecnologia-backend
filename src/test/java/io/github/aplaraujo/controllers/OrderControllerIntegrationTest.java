package io.github.aplaraujo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.aplaraujo.test.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(properties = {
        "JWT_SECRET=8D!x:QBlgd6Hr5ME6f@sVR+Sj2EY3;InZyRDo)nQQ9C",
        "JWT_DURATION:86400"
})
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

//    private String adminEmail, adminPassword, adminToken;
//    private String clientEmail, clientPassword, clientToken;
//    private Long existentOrderId, nonExistentOrderId;
//
//    @BeforeEach
//    public void setUp() throws Exception {
//        adminEmail = "carolinenairdacosta@outllok.com";
//        adminPassword = "qo7w7BN7wX";
//        adminToken = tokenUtil.obtainAccessTokenForTest(mockMvc, adminEmail, adminPassword);
//
//        clientEmail = "hugovitormartins@iclud.com";
//        clientPassword = "AgZ463qGli";
//        clientToken = tokenUtil.obtainAccessTokenForTest(mockMvc, clientEmail, clientPassword);
//
//        existentOrderId = 1L;
//        nonExistentOrderId = 1000L;
//
//
//    }

    @Test
    public void findOrdersFullShouldReturn200WhenAdminIsLogged() throws Exception {
        String adminToken = tokenUtil.obtainAccessTokenForTest(mockMvc,"carolinenairdacosta@outllok.com", "qo7w7BN7wX");
        ResultActions result = mockMvc.perform(get("/order-list").header("Authorization", "Bearer " + adminToken).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @Test
    public void findOrdersFullShouldReturn401WhenAdminIsNotLogged() throws Exception {
        ResultActions result = mockMvc.perform(get("/order-list").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void findOrderByIdShouldReturn200WhenClientIsLoggedAndIdExists() throws Exception {
        Long existentOrderId = 1L;
        String clientToken = tokenUtil.obtainAccessTokenForTest(mockMvc, "hugovitormartins@iclud.com", "AgZ463qGli");
        ResultActions result = mockMvc.perform(get("/orders/" + existentOrderId).header("Authorization", "Bearer " + clientToken).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existentOrderId));
        result.andExpect(jsonPath("$.client_id").value(2L));
        result.andExpect(jsonPath("$.product_id").value(2L));
        result.andExpect(jsonPath("$.created_At").value("2026-01-12T18:19:20.673204744Z"));
        result.andExpect(jsonPath("$.order_status").value("EM_PREPARO"));
    }
//
//    @Test
//    public void findOrderByIdShouldReturn404WhenClientIsLoggedAndIdDoesNotExist() throws Exception {
//        ResultActions result = mockMvc.perform(get("/orders/" + nonExistentOrderId).header("Authorization", "Bearer " + clientToken).accept(MediaType.APPLICATION_JSON));
//        result.andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void findOrderByIdShouldReturn401WhenClientIsNotLogged() throws Exception {
//        ResultActions result = mockMvc.perform(get("/orders/" + existentOrderId).accept(MediaType.APPLICATION_JSON));
//        result.andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void findOrderByIdShouldReturn403WhenAdminIsLogged() throws Exception {
//        ResultActions result = mockMvc.perform(get("/orders/" + existentOrderId).header("Authorization", "Bearer " + adminToken).accept(MediaType.APPLICATION_JSON));
//        result.andExpect(status().isNotFound());
//    }

    @Test
    public void insertOrderShouldReturn200WhenClientIsLoggedAndAllDataValid() {
        // OrderDTO dto = new OrderDTO(2L, 7L, "2026-01-12T18:19:20.673204744Z", "EM_ENTREGA");
        //        ResultActions result = mockMvc.perform(post("/orders")
//                .header("Authorization", "Bearer " + clientToken)
//                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
        // result.andExpect(status().isCreated());
    }

    @Test
    public void insertOrderShouldReturn422WhenClientIsLoggedAndClientIdFieldIsNegativeOrEqualToZero() {
        // OrderDTO dto = new OrderDTO(0L, 7L, "2026-01-12T18:19:20.673204744Z", "EM_ENTREGA");
        //        ResultActions result = mockMvc.perform(post("/orders")
//                .header("Authorization", "Bearer " + clientToken)
        //        result.andExpect(status().isUnprocessableEntity());
//        result.andExpect(jsonPath("$.status").value(422));
//        result.andExpect(jsonPath("$.message").value("Invalid data"));
//        result.andExpect(jsonPath("$.errors[0].field").value("client_id"));
//        result.andExpect(jsonPath("$.errors[0].error").value("This id should be positive"));
    }

    @Test
    public void insertOrderShouldReturn422WhenClientIsLoggedAndProductIdFieldIsNegativeOrEqualToZero() {
        // OrderDTO dto = new OrderDTO(2L, 0L, "2026-01-12T18:19:20.673204744Z", "EM_ENTREGA");
        //        ResultActions result = mockMvc.perform(post("/orders")
//                .header("Authorization", "Bearer " + clientToken)
        //        result.andExpect(status().isUnprocessableEntity());
//        result.andExpect(jsonPath("$.status").value(422));
//        result.andExpect(jsonPath("$.message").value("Invalid data"));
//        result.andExpect(jsonPath("$.errors[0].field").value("product_id"));
//        result.andExpect(jsonPath("$.errors[0].error").value("This id should be positive"));
    }

    @Test
    public void insertOrderShouldReturn422WhenClientIsLoggedAndOrderStatusFieldIsNull() {
        // OrderDTO dto = new OrderDTO(0L, 7L, "2026-01-12T18:19:20.673204744Z", null);
        //        ResultActions result = mockMvc.perform(post("/orders")
//                .header("Authorization", "Bearer " + clientToken)
        //        result.andExpect(status().isUnprocessableEntity());
//        result.andExpect(jsonPath("$.status").value(422));
//        result.andExpect(jsonPath("$.message").value("Invalid data"));
//        result.andExpect(jsonPath("$.errors[0].field").value("order_status"));
//        result.andExpect(jsonPath("$.errors[0].error").value("This field should not be null"));
    }

    @Test
    public void insertOrderShouldReturn401WhenClientIsNotLogged() {
        // OrderDTO dto = new OrderDTO(2L, 7L, "2026-01-12T18:19:20.673204744Z", "EM_ENTREGA");
        //        ResultActions result = mockMvc.perform(post("/orders")
//                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
//        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void insertOrderShouldReturn403WhenAdminIsLogged() {
        // OrderDTO dto = new OrderDTO(2L, 7L, "2026-01-12T18:19:20.673204744Z", "EM_ENTREGA");
        //        ResultActions result = mockMvc.perform(post("/orders")
//                .header("Authorization", "Bearer " + adminToken)
//                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
//        result.andExpect(status().isForbidden());
    }

    @Test
    public void updateOrderShouldReturn204WhenClientIsLoggedAndAllDataValid() {
        // OrderDTO dto = new OrderDTO(existentOrderId, 2L, 2L, "2026-01-12T18:19:20.673204744Z", "ENTREGUE");
        //        ResultActions result = mockMvc.perform(put("/orders/" + existentOrderId)
//                .header("Authorization", "Bearer " + clientToken)
//                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
//        result.andExpect(status().isNoContent());
    }

    @Test
    public void updateOrderShouldReturn422WhenClientIsLoggedAndClientIdFieldIsNegativeOrEqualToZero() {
        // OrderDTO dto = new OrderDTO(existentOrderId, 0L, 2L, "2026-01-12T18:19:20.673204744Z", "ENTREGUE");
        //        ResultActions result = mockMvc.perform(put("/orders/" + existentOrderId)
//                .header("Authorization", "Bearer " + clientToken)
        //        result.andExpect(status().isUnprocessableEntity());
//        result.andExpect(jsonPath("$.status").value(422));
//        result.andExpect(jsonPath("$.message").value("Invalid data"));
//        result.andExpect(jsonPath("$.errors[0].field").value("client_id"));
//        result.andExpect(jsonPath("$.errors[0].error").value("This id should be positive"));
    }

    @Test
    public void updateOrderShouldReturn422WhenClientIsLoggedAndProductIdFieldIsNegativeOrEqualToZero() {
        // OrderDTO dto = new OrderDTO(existentOrderId, 2L, 0L, "2026-01-12T18:19:20.673204744Z", "ENTREGUE");
        //        ResultActions result = mockMvc.perform(put("/orders/" + existentOrderId)
//                .header("Authorization", "Bearer " + clientToken)
        //        result.andExpect(status().isUnprocessableEntity());
//        result.andExpect(jsonPath("$.status").value(422));
//        result.andExpect(jsonPath("$.message").value("Invalid data"));
//        result.andExpect(jsonPath("$.errors[0].field").value("product_id"));
//        result.andExpect(jsonPath("$.errors[0].error").value("This id should be positive"));
    }

    @Test
    public void updateOrderShouldReturn422WhenClientIsLoggedAndOrderStatusFieldIsNull() {
        // OrderDTO dto = new OrderDTO(existentOrderId, 2L, 2L, "2026-01-12T18:19:20.673204744Z", null);
        //        ResultActions result = mockMvc.perform(put("/orders/" + existentOrderId)
//                .header("Authorization", "Bearer " + clientToken)
        //        result.andExpect(status().isUnprocessableEntity());
//        result.andExpect(jsonPath("$.status").value(422));
//        result.andExpect(jsonPath("$.message").value("Invalid data"));
//        result.andExpect(jsonPath("$.errors[0].field").value("order_status"));
//        result.andExpect(jsonPath("$.errors[0].error").value("This field should not be null"));
    }

    @Test
    public void updateOrderShouldReturn401WhenClientIsNotLogged() {
        // OrderDTO dto = new OrderDTO(existentOrderId, 2L, 2L, "2026-01-12T18:19:20.673204744Z", "ENTREGUE");
        //        ResultActions result = mockMvc.perform(put("/orders/" + existentOrderId)
//                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
//        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void updateOrderShouldReturn403WhenAdminIsLogged() {
        // OrderDTO dto = new OrderDTO(existentOrderId, 2L, 2L, "2026-01-12T18:19:20.673204744Z", "ENTREGUE");
        //        ResultActions result = mockMvc.perform(put("/orders/" + existentOrderId)
//                .header("Authorization", "Bearer " + adminToken)
//                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
//        result.andExpect(status().isForbidden());
    }

//    @Test
//    public void deleteOrderShouldReturn204WhenClientIsLoggedAndIdExists() throws Exception {
//        ResultActions result = mockMvc.perform(delete("/orders/" + existentOrderId).header("Authorization", "Bearer " + clientToken).contentType(MediaType.APPLICATION_JSON));
//        result.andExpect(status().isNoContent());
//    }
//
//    @Test
//    public void deleteOrderShouldReturn404WhenClientIsLoggedAndIdDoesNotExist() throws Exception {
//        ResultActions result = mockMvc.perform(delete("/orders/" + nonExistentOrderId).header("Authorization", "Bearer " + clientToken).contentType(MediaType.APPLICATION_JSON));
//        result.andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void deleteOrderShouldReturn401WhenClientIsNotLogged() throws Exception {
//        ResultActions result = mockMvc.perform(delete("/orders/" + existentOrderId).contentType(MediaType.APPLICATION_JSON));
//        result.andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void deleteOrderShouldReturn403WhenAdminIsLogged() throws Exception {
//        ResultActions result = mockMvc.perform(delete("/orders/" + existentOrderId).header("Authorization", "Bearer " + adminToken).contentType(MediaType.APPLICATION_JSON));
//        result.andExpect(status().isForbidden());
//    }
}
