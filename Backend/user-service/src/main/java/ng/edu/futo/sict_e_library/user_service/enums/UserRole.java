package ng.edu.futo.sict_e_library.user_service.enums;

public enum UserRole {
    ADMIN_STAFF("Admin Staff - Full system access"),
    ACADEMIC_STAFF("Academic Staff - Upload, access and manage academic materials"),
    STUDENT("Student - Upload projects and access materials"),
    GUEST("Guest - Read-only access to public materials");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}