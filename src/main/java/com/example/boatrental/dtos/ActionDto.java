package com.example.boatrental.dtos;

public class ActionDto {
    private String href;
    private String method;
    private String contentType;

    public ActionDto() {
    }

    public ActionDto(String href, String method, String contentType) {
        this.href = href;
        this.method = method;
        this.contentType = contentType;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "ActionDto{" +
                "href='" + href + '\'' +
                ", method='" + method + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
