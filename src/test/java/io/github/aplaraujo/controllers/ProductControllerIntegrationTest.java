package io.github.aplaraujo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.aplaraujo.dto.ProductDTO;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "JWT_SECRET=mFiRiSPbDQIEIKztvpVKjSOVCo6SgcPWCdU9x4ADFH0",
        "JWT_DURATION=86400"
})
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private String existentEmail, existentPassword, existentClientEmail, existentClientPassword, clientToken, token;
    private Long existentProductId, nonExistentProductId, countTotalProducts;

    @BeforeEach
    public void setUp() throws Exception {
        existentEmail = "carolinenairdacosta@outllok.com";
        existentPassword = "qo7w7BN7wX";
        existentClientEmail = "hugovitormartins@iclud.com";
        existentClientPassword = "AgZ463qGli";
        existentProductId = 1L;
        nonExistentProductId = 1000L;
        countTotalProducts = 18L;

        token = tokenUtil.obtainAccessTokenForTest(mockMvc, existentEmail, existentPassword);
        clientToken = tokenUtil.obtainAccessTokenForTest(mockMvc, existentClientEmail, existentClientPassword);
    }

    @Test
    public void findProductsShouldReturn200WhenTheProductListIsOrderedByName() throws Exception {
        ResultActions result = mockMvc.perform(get("/catalog/products?page=0&size=12&sort=name,asc").accept(MediaType.APPLICATION_JSON));
        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
        result.andExpect(jsonPath("$.content").exists());
        result.andExpect(jsonPath("$.content[0].name").value("Americano"));
        result.andExpect(jsonPath("$.content[1].name").value("Burger BBQ"));
        result.andExpect(jsonPath("$.content[2].name").value("Burger Bacon"));
        result.andExpect(jsonPath("$.content[3].name").value("Burger Cheddar"));
    }

    @Test
    public void findProductByIdShouldReturn200WhenAdminIsLoggedAndIdExists() throws Exception {
        ResultActions result = mockMvc.perform(get("/products/" + existentProductId).header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existentProductId));
        result.andExpect(jsonPath("$.name").value("X-Burger"));
        result.andExpect(jsonPath("$.price").value(12.0));
    }

    @Test
    public void findProductByIdShouldReturn404whenAdminIsLoggedAndIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(get("/products/" + nonExistentProductId).header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void findProductByIdShouldReturn401WhenAdminIsNotLogged() throws Exception {
        ResultActions result = mockMvc.perform(get("/products/" + existentProductId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void insertProductShouldReturn201WhenAdminIsLoggedAndAllDataIsValid() throws Exception {
        ProductDTO dto = new ProductDTO(19L,"Batata Frita Pequena", 10.0);
        ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
         result.andExpect(status().isCreated());
    }

    @Test
    public void insertProductShouldReturn422WhenAdminIsLoggedAndNameFieldIsEmpty() throws Exception {
         ProductDTO dto = new ProductDTO(19L, "   ", 10.0);
                ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.message").value("Invalid data"));
        result.andExpect(jsonPath("$.errors[0].field").value("name"));
        result.andExpect(jsonPath("$.errors[0].error").value("This field should not be empty"));
    }

    @Test
    public void insertProductShouldReturn422WhenAdminIsLoggedAndNameFieldIsOutTheRangeFrom3To100Characters() throws Exception {
         ProductDTO dto = new ProductDTO(19L, "Ba", 10.0);
                ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
                result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.message").value("Invalid data"));
        result.andExpect(jsonPath("$.errors[0].field").value("name"));
        result.andExpect(jsonPath("$.errors[0].error").value("The name should have from 3 to 100 characters"));
    }

    @Test
    public void insertProductShouldReturn422WhenAdminIsLoggedAndPriceFieldIsNegativeOrEqualToZero() throws Exception {
         ProductDTO dto = new ProductDTO(19L, "Batata Frita Pequena", 0.0);
                ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
                result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.message").value("Invalid data"));
        result.andExpect(jsonPath("$.errors[0].field").value("price"));
        result.andExpect(jsonPath("$.errors[0].error").value("The price should be positive"));
    }

    @Test
    public void insertProductShouldReturn401WhenAdminIsNotLogged() {
        // ProductDTO dto = new ProductDTO("Batata Frita Pequena", 10.0);
//        ResultActions result = mockMvc.perform(post("/products")
//                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
        // result.andExpect(status().isUnauthorized());
    }

    @Test
    public void insertProdictShouldReturn403WhenClientIsLogged() {
        // ProductDTO dto = new ProductDTO("Batata Frita Pequena", 10.0);
//        ResultActions result = mockMvc.perform(post("/products")
//                .header("Authorization", "Bearer " + clientToken)
//                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
//         result.andExpect(status().isForbidden());
    }

    @Test
    public void updateProductShouldReturn200WhenAdminIsLoggedAndAllDataIsValid() throws Exception {
        // ProductDTO dto = new ProductDTO(existentProductId, "X-Burger Especial", 10.5);
//        ResultActions result = mockMvc.perform(put("/products" + existentProductId)
//                .header("Authorization", "Bearer " + token)
//                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
        // result.andExpect(status().isOk());
        // result.andExpect(jsonPath("$.id").value(existentProductId));
        // result.andExpect(jsonPath("$.name").value("X-Burger Especial"));
        // result.andExpect(jsonPath("$.price").value(10.5));
    }

    @Test
    public void updateProductShouldReturn422WhenAdminIsLoggedAndTheNameFieldIsEmpty() throws Exception {
        // ProductDTO dto = new ProductDTO(existentProductId, "   ", 10.5);
//        ResultActions result = mockMvc.perform(put("/products" + existentProductId)
//                .header("Authorization", "Bearer " + token)
//                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
//        result.andExpect(status().isUnprocessableEntity());
//        result.andExpect(jsonPath("$.status").value(422));
//        result.andExpect(jsonPath("$.message").value("Invalid data"));
//        result.andExpect(jsonPath("$.errors[0].field").value("name"));
//        result.andExpect(jsonPath("$.errors[0].error").value("This field should not be empty"));
    }

    @Test
    public void updateProductShouldReturn422WhenAdminIsLoggedAndTheNameFieldIsOutTheRangeFrom3To100Characters() throws Exception {
        // ProductDTO dto = new ProductDTO(existentProductId, "X", 10.5);
//        ResultActions result = mockMvc.perform(put("/products" + existentProductId)
//                .header("Authorization", "Bearer " + token)
//                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
        //        result.andExpect(status().isUnprocessableEntity());
//        result.andExpect(jsonPath("$.status").value(422));
//        result.andExpect(jsonPath("$.message").value("Invalid data"));
//        result.andExpect(jsonPath("$.errors[0].field").value("name"));
//        result.andExpect(jsonPath("$.errors[0].error").value("The name should have from 3 to 100 characters"));
    }

    @Test
    public void updateProductShouldReturn422WhenAdminIsLoggedAndThePriceFieldIsNegativeOrEqualToZero() throws Exception {
        // ProductDTO dto = new ProductDTO(existentProductId, "X-Burger Especial", 0.0);
//        ResultActions result = mockMvc.perform(put("/products" + existentProductId)
//                .header("Authorization", "Bearer " + token)
//                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
        //        result.andExpect(status().isUnprocessableEntity());
//        result.andExpect(jsonPath("$.status").value(422));
//        result.andExpect(jsonPath("$.message").value("Invalid data"));
//        result.andExpect(jsonPath("$.errors[0].field").value("price"));
//        result.andExpect(jsonPath("$.errors[0].error").value("The price should be positive"));
    }

    @Test
    public void updateProductShouldReturn401WhenAdminIsNotLogged() throws Exception {
        // ProductDTO dto = new ProductDTO(existentProductId, "X-Burger Especial", 10.5);
//        ResultActions result = mockMvc.perform(put("/products" + existentProductId)
//                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
        // result.andExpect(status().isUnauthorized());
    }

    @Test
    public void updateProductShouldReturn403WhenClientIsLogged() throws Exception {
        // ProductDTO dto = new ProductDTO(existentProductId, "X-Burger Especial", 10.5);
//        ResultActions result = mockMvc.perform(put("/products" + existentProductId)
//                .header("Authorization", "Bearer " + clientToken)
//                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON));
        //         result.andExpect(status().isForbidden());
    }

    @Test
    public void deleteProductShouldReturn204WhenAdminIsLoggedAndIdExists() throws Exception {
        ResultActions result = mockMvc.perform(delete("/todos/" + existentProductId).header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteProductShouldReturn401WhenAdminIsNotLogged() throws Exception {
        ResultActions result = mockMvc.perform(delete("/todos/" + existentProductId).contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteProductShouldReturn403WhenClientIsLogged() throws Exception {
        ResultActions result = mockMvc.perform(delete("/todos/" + existentProductId).header("Authorization", "Bearer " + clientToken).contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isForbidden());
    }
}
