package za.co.dearx.leave.web.rest.errors;

public class UserNotFoundException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
        super("User could not be retrieved", "userManagement", "usernotfound");
    }
}
