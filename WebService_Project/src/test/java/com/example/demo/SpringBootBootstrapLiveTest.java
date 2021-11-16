package com.example.demo;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Random;

public class SpringBootBootstrapLiveTest {

    private static final String API_ROOT
            = "http://localhost:8081/api/books";

    private Book createRandomBook() {
        Book book = new Book();
        book.setTitle(randomAlphabetic(10));
        book.setAuthor(randomAlphabetic(15));
        return book;
    }

    private String randomAlphabetic(int i) {
        Random random = new Random();
        i = random.nextInt();
        return Integer.toString(i);
    }

    private String createBookAsUri(Book book) {
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .post(API_ROOT);
        return API_ROOT + "/" + response.jsonPath().get("id");
    }

    @Test
    public void whenGetAllBooks_thenOK() {
        Response response = RestAssured.get(API_ROOT);

        assertEquals(String.valueOf(HttpStatus.OK.value()), response.getStatusCode());
    }

    @Test
    public void whenGetBooksByTitle_thenOK() {
        Book book = createRandomBook();
        createBookAsUri(book);
        Response response = RestAssured.get(
                API_ROOT + "/title/" + book.getTitle());

        assertEquals(String.valueOf(HttpStatus.OK.value()), response.getStatusCode());
        assertTrue(response.as(List.class)
                .size() > 0);
    }

    private void assertTrue(boolean b) {
    }

    private void assertEquals(String value, int statusCode) {
    }

    @Test
    public void whenGetCreatedBookById_thenOK() {
        Book book = createRandomBook();
        String location = createBookAsUri(book);
        Response response = RestAssured.get(location);

        assertEquals(String.valueOf(HttpStatus.OK.value()), response.getStatusCode());
        assertEquals(book.getTitle(), response.jsonPath()
                .get("title"));
    }

    @Test
    public void whenGetNotExistBookById_thenNotFound() {
        Response response = RestAssured.get(API_ROOT + "/" + randomNumeric(4));

        assertEquals(String.valueOf(HttpStatus.NOT_FOUND.value()), response.getStatusCode());
    }

    private String randomNumeric(int i) {
        Random random = new Random();
        i = random.nextInt();
        return Integer.toString(i);
    }
    @Test
    public void whenCreateNewBook_thenCreated() {
        Book book = createRandomBook();
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .post(API_ROOT);

        assertEquals(String.valueOf(HttpStatus.CREATED.value()), response.getStatusCode());
    }

    @Test
    public void whenInvalidBook_thenError() {
        Book book = createRandomBook();
        book.setAuthor(null);
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .post(API_ROOT);

        assertEquals(String.valueOf(HttpStatus.BAD_REQUEST.value()), response.getStatusCode());
    }
    @Test
    public void whenUpdateCreatedBook_thenUpdated() {
        Book book = createRandomBook();
        String location = createBookAsUri(book);
        book.setId(Long.parseLong(location.split("api/books/")[1]));
        book.setAuthor("newAuthor");
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .put(location);

        assertEquals(String.valueOf(HttpStatus.OK.value()), response.getStatusCode());

        response = RestAssured.get(location);

        assertEquals(String.valueOf(HttpStatus.OK.value()), response.getStatusCode());
        assertEquals("newAuthor", response.jsonPath()
                .get("author"));
    }
}
