import java.util.EventListener;

public interface ForbiddenWordListener extends EventListener {
    void forbiddenWordFound(ForbiddenWordEvent event);
}
