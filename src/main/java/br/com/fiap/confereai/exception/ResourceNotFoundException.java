package br.com.fiap.confereai.exception;


public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String recurso, Long id) {
        super(recurso + " com id " + id + " não encontrado(a)");
    }
}
