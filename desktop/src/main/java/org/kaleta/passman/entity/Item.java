package org.kaleta.passman.entity;

/**
 * User: Stanislav Kaleta
 * Date: 23.6.2015
 * TODO ! attributy maju maximalnu dlzku 30 znakov a nemozu obsahovat znak '$' !
 */
public class Item {
    private String name;
    private String login;
    private String password;
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (link != null ? !link.equals(item.link) : item.link != null) return false;
        if (login != null ? !login.equals(item.login) : item.login != null) return false;
        if (name != null ? !name.equals(item.name) : item.name != null) return false;
        if (password != null ? !password.equals(item.password) : item.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        return result;
    }
}
