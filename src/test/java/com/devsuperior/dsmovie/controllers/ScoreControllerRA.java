package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class ScoreControllerRA {

	private String clientUsername, clientPassword, adminUsername, adminPassword;
	private String clientToken, adminToken, invalidToken;
	private Map<String, Object> putScore;

	@BeforeEach
	public void setUp() throws Exception {
		baseURI = "http://localhost:8080";

		putScore = new HashMap<>();
		putScore.put("movieId", 1L);
		putScore.put("score", 4);

		clientUsername = "ana@gmail.com";
		clientPassword = "123456";
		adminUsername = "maria@gmail.com";
		adminPassword = "123456";

		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
		invalidToken = adminToken + "xpto";
	}

	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		putScore.put("movieId", 100L);
		JSONObject score = new JSONObject(putScore);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(score)
				.when()
				.put("/scores")
				.then()
				.statusCode(404)
				.body("error", equalTo("Recurso não encontrado"))
				.body("status", equalTo(404));
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
		putScore.remove("movieId");
		JSONObject score = new JSONObject(putScore);
		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(score)
				.when()
				.put("/scores")
				.then()
				.statusCode(422)
				.body("errors[0].fieldName", equalTo("movieId"))
				.body("errors[0].message", equalTo("Campo requerido"));
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {		
	}
}
