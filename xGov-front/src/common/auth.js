import Cookies from "js-cookie";

const Token = "token";

export default {
  getToken() {
    return Cookies.get(Token);
  },
  setToken(token) {
    return Cookies.set(Token, token);
  },
  removeToken() {
    return Cookies.remove(Token);
  }
};
