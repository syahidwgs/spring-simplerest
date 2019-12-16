package com.syhdmf.simplerest.helper;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import com.syhdmf.simplerest.dto.response.BaseResponse;

public class JsonResponseUtil {

	public static <T> ResponseEntity<BaseResponse<T>> bsuccess(BaseResponse<T> baseResponse) {
		baseResponse.setStatus(200);
		baseResponse.setError("OK");
		return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
	}
	
	public static <T> ResponseEntity<BaseResponse<T>> success(T data) {
		BaseResponse<T> baseResponse = new BaseResponse<>();
		baseResponse.setData(data);
		return bsuccess(baseResponse);
	}
	
	public static <T> ResponseEntity<BaseResponse<T>> success(T data, String message) {
		BaseResponse<T> baseResponse = new BaseResponse<>();
		baseResponse.setData(data);
		baseResponse.setMessage(message);
		return bsuccess(baseResponse);
	}
	
	public static <T> ResponseEntity<BaseResponse<T>> berror(BaseResponse<T> baseResponse) {
		baseResponse.setStatus(400);
		baseResponse.setError("Bad Request");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
	}
	
	public static <T> ResponseEntity<BaseResponse<T>> error(T data) {
		BaseResponse<T> baseResponse = new BaseResponse<>();
		baseResponse.setData(data);
		return berror(baseResponse);
	}
	
	public static <T> ResponseEntity<BaseResponse<T>> error(T data, String message) {
		BaseResponse<T> baseResponse = new BaseResponse<>();
		baseResponse.setData(data);
		baseResponse.setMessage(message);
		return berror(baseResponse);
	}
	
	public static <T> ResponseEntity<BaseResponse<T>> error(List<FieldError> errors) {
		BaseResponse<T> baseResponse = new BaseResponse<>();
		String message = "";
		for (FieldError row: errors) {
			message += row.getField() + " " + row.getDefaultMessage() + ". ";
		}
		baseResponse.setMessage(message);
		return berror(baseResponse);
	}
	
	public static <T> ResponseEntity<BaseResponse<T>> bbase(BaseResponse<T> baseResponse) {
		baseResponse.setError((baseResponse.getStatus() >= 200 && baseResponse.getStatus() < 300 ? "OK" : "Something Wrong"));
		return ResponseEntity.status(HttpStatus.valueOf(baseResponse.getStatus())).body(baseResponse);
	}
	
	public static <T> ResponseEntity<BaseResponse<T>> base(int status, String message) {
		BaseResponse<T> baseResponse = new BaseResponse<>();
		baseResponse.setStatus(status);
		baseResponse.setMessage(message);
		return bbase(baseResponse);
	}
}
