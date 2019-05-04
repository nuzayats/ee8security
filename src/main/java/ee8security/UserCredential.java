package ee8security;

import java.util.Objects;

public class UserCredential {

    final long companyId;
    final String email;
    final String password;

    public UserCredential(long companyId, String email, String password) {
        this.companyId = companyId;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCredential that = (UserCredential) o;
        return companyId == that.companyId &&
                email.equals(that.email) &&
                password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, email, password);
    }
}
