package io.github.aplaraujo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.aplaraujo.dto.OrderDTO;
import io.github.aplaraujo.dto.ProductDTO;
import io.github.aplaraujo.entities.enums.OrderStatus;
import io.github.aplaraujo.test.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(properties = {
        "JWT_SECRET=mFiRiSPbDQIEIKztvpVKjSOVCo6SgcPWCdU9x4ADFH0",
        "JWT_DURATION=86400"
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

    private String existentEmail, existentPassword, existentClientEmail, existentClientPassword, clientToken, token;
    private Long existentOrderId, nonExistentOrderId;

    @BeforeEach
    public void setUp() throws Exception {
        existentEmail = "carolinenairdacosta@outllok.com";
        existentPassword = "qo7w7BN7wX";
        existentClientEmail = "hugovitormartins@iclud.com";
        existentClientPassword = "AgZ463qGli";
        existentOrderId = 1L;
        nonExistentOrderId= 1000L;

        token = tokenUtil.obtainAccessTokenForTest(mockMvc, existentEmail, existentPassword);
        clientToken = tokenUtil.obtainAccessTokenForTest(mockMvc, existentClientEmail, existentClientPassword);
    }

    @Test
    public void findOrdersFullShouldReturn200WhenAdminIsLogged() throws Exception {
        mockMvc.perform(get("/order-list").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findOrdersFullShouldReturn401WhenAdminIsNotLogged() throws Exception {
        mockMvc.perform(get("/order-list").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void findOrdersShouldReturn200WhenClientIsLogged() throws Exception {
        mockMvc.perform(get("/orders").header("Authorization", "Bearer " + clientToken).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findOrdersShouldReturn401WhenClientIsNotLogged() throws Exception {
        mockMvc.perform(get("/orders").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void findOrdersShouldReturn403WhenAdminIsLogged() throws Exception {
        mockMvc.perform(get("/orders").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void findOrderByIdShouldReturn200WhenClientIsLoggedAndIdExists() throws Exception {
        mockMvc.perform(get("/orders/" + existentOrderId).header("Authorization", "Bearer " + clientToken).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existentOrderId))
                .andExpect(jsonPath("$.clientId").value(2L))
                .andExpect(jsonPath("$.products[0].id").value(2L))
                .andExpect(jsonPath("$.products[0].name").value("X-Salada"))
                .andExpect(jsonPath("$.products[0].price").value(14.0))
                .andExpect(jsonPath("$.orderStatus").value("EM_PREPARO"));
    }

    @Test
    public void findOrderByIdShouldReturn404WhenClientIsLoggedAndIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/orders/" + nonExistentOrderId).header("Authorization", "Bearer " + clientToken).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findOrderByIdShouldReturn401WhenClientIsNotLogged() throws Exception {
        mockMvc.perform(get("/orders/" + existentOrderId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void findOrderByIdShouldReturn403WhenAdminIsLogged() throws Exception {
        mockMvc.perform(get("/orders/" + existentOrderId).header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void insertOrderShouldReturn201WhenClientIsLoggedAndAllDataIsValid() throws Exception {
        OrderDTO dto = new OrderDTO(6L, 2L, List.of(new ProductDTO(9L, "Burger Clássico", 18.0), new ProductDTO(10L, "Burger Cheddar", 20.0)), OrderStatus.EM_PREPARO);
        mockMvc.perform(post("/orders").header("Authorization", "Bearer " + clientToken).content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void insertOrderShouldReturn422WhenClientIsLoggedAndClientIdFieldIsNegativeOrEqualToZero() throws Exception {
        OrderDTO dto = new OrderDTO(6L, 0L, List.of(new ProductDTO(9L, "Burger Clássico", 18.0), new ProductDTO(10L, "Burger Cheddar", 20.0)), OrderStatus.EM_PREPARO);
        mockMvc.perform(post("/orders").header("Authorization", "Bearer " + clientToken).content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.message").value("Invalid data"))
                .andExpect(jsonPath("$.errors[0].field").value("clientId"))
                .andExpect(jsonPath("$.errors[0].error").value("This id should be positive"));
    }

    @Test
    public void insertOrderShouldReturn422WhenClientIsLoggedAndProductsArrayIsEmpty() throws Exception {
        OrderDTO dto = new OrderDTO(6L, 2L, List.of(), OrderStatus.EM_PREPARO);
        mockMvc.perform(post("/orders").header("Authorization", "Bearer " + clientToken).content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.message").value("Invalid data"))
                .andExpect(jsonPath("$.errors[0].field").value("products"))
                .andExpect(jsonPath("$.errors[0].error").value("This array should have at least one product"));
    }

    @Test
    public void insertOrderShouldReturn422WhenClientIsLoggedAndOrderStatusFieldIsNull() throws Exception {
        OrderDTO dto = new OrderDTO(6L, 2L, List.of(new ProductDTO(9L, "Burger Clássico", 18.0), new ProductDTO(10L, "Burger Cheddar", 20.0)), null);
        mockMvc.perform(post("/orders").header("Authorization", "Bearer " + clientToken).content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.message").value("Invalid data"))
                .andExpect(jsonPath("$.errors[0].field").value("orderStatus"))
                .andExpect(jsonPath("$.errors[0].error").value("This field should not be null"));
    }

    @Test
    public void insertOrderShouldReturn401WhenClientIsNotLogged() throws Exception {
        OrderDTO dto = new OrderDTO(6L, 2L, List.of(new ProductDTO(9L, "Burger Clássico", 18.0), new ProductDTO(10L, "Burger Cheddar", 20.0)), OrderStatus.EM_PREPARO);
        mockMvc.perform(post("/orders").content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void insertOrderShouldReturn403WhenAdminIsLogged() throws Exception {
        OrderDTO dto = new OrderDTO(6L, 2L, List.of(new ProductDTO(9L, "Burger Clássico", 18.0), new ProductDTO(10L, "Burger Cheddar", 20.0)), OrderStatus.EM_PREPARO);
        mockMvc.perform(post("/orders").header("Authorization", "Bearer " + token).content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateOrderShouldReturn200WhenClientIsLoggedAndAllDataValid() throws Exception {
        OrderDTO dto = new OrderDTO(existentOrderId, 2L, List.of(new ProductDTO(2L, "X-Salada", 14.0)), OrderStatus.ENTREGUE);
        mockMvc.perform(put("/orders/" + existentOrderId)
                        .header("Authorization", "Bearer " + clientToken)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateOrderShouldReturn422WhenClientIsLoggedAndClientIdFieldIsNegativeOrEqualToZero() throws Exception {
        OrderDTO dto = new OrderDTO(existentOrderId, 0L, List.of(new ProductDTO(2L, "X-Salada", 14.0)), OrderStatus.ENTREGUE);
        mockMvc.perform(put("/orders/" + existentOrderId)
                        .header("Authorization", "Bearer " + clientToken)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.message").value("Invalid data"))
                .andExpect(jsonPath("$.errors[0].field").value("clientId"))
                .andExpect(jsonPath("$.errors[0].error").value("This id should be positive"));
    }

    @Test
    public void updateOrderShouldReturn422WhenClientIsLoggedAndProductsArrayIsEmpty() throws Exception {
        OrderDTO dto = new OrderDTO(existentOrderId, 2L, List.of(), OrderStatus.ENTREGUE);
        mockMvc.perform(put("/orders/" + existentOrderId)
                        .header("Authorization", "Bearer " + clientToken)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.message").value("Invalid data"))
                .andExpect(jsonPath("$.errors[0].field").value("products"))
                .andExpect(jsonPath("$.errors[0].error").value("This array should have at least one product"));
    }

    @Test
    public void updateOrderShouldReturn422WhenClientIsLoggedAndOrderStatusFieldIsNull() throws Exception {
        OrderDTO dto = new OrderDTO(existentOrderId, 2L, List.of(new ProductDTO(2L, "X-Salada", 14.0)), null);
        mockMvc.perform(put("/orders/" + existentOrderId)
                        .header("Authorization", "Bearer " + clientToken)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.message").value("Invalid data"))
                .andExpect(jsonPath("$.errors[0].field").value("orderStatus"))
                .andExpect(jsonPath("$.errors[0].error").value("This field should not be null"));
    }

    @Test
    public void updateOrderShouldReturn401WhenClientIsNotLogged() throws Exception {
        OrderDTO dto = new OrderDTO(existentOrderId, 2L, List.of(new ProductDTO(2L, "X-Salada", 14.0)), OrderStatus.ENTREGUE);
        mockMvc.perform(put("/orders/" + existentOrderId)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void updateOrderShouldReturn403WhenAdminIsLogged() throws Exception {
        OrderDTO dto = new OrderDTO(existentOrderId, 2L, List.of(new ProductDTO(2L, "X-Salada", 14.0)), OrderStatus.ENTREGUE);
        mockMvc.perform(put("/orders/" + existentOrderId)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteOrderShouldReturn204WhenClientIsLoggedAndIdExists() throws Exception {
        mockMvc.perform(delete("/orders/" + existentOrderId).header("Authorization", "Bearer " + clientToken).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteOrderShouldReturn404WhenClientIsLoggedAndIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/orders/" + nonExistentOrderId).header("Authorization", "Bearer " + clientToken).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteOrderShouldReturn401WhenClientIsNotLogged() throws Exception {
        mockMvc.perform(delete("/orders/" + existentOrderId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteOrderShouldReturn403WhenAdminIsLogged() throws Exception {
        mockMvc.perform(delete("/orders/" + existentOrderId).header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
