package app.groopy.threadsservice.domain.exceptions;

public class EntityNotFoundException extends Throwable {
    public EntityNotFoundException(String entityType, String id) {
        super("Entity " + entityType + " not found for ID " + id);
    }
}
