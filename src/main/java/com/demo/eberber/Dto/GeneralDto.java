package com.demo.eberber.Dto;

public class GeneralDto {
    public class Response {
        public Object data;
        public boolean Error;
        public String Message;

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }

        public boolean isError() {
            return Error;
        }

        public void setError(boolean Error) {
            this.Error = Error;
        }
    }
}
