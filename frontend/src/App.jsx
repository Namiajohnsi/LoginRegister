
import { useState } from "react";
import "./App.css";

function App() {
  const [isLogin, setIsLogin] = useState(true);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  async function handleSubmit(e) {
    e.preventDefault();
    setMessage("Please wait...");

    const data = new URLSearchParams();
    if (!isLogin) data.append("name", name);
    data.append("email", email);
    data.append("password", password);

    try {
      const response = await fetch(
        `http://localhost:8080/login-backend/api/${
          isLogin ? "login" : "register"
        }`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          body: data
        }
      );

      const result = await response.text();
      setMessage(result);

      if (response.ok && !isLogin) {
        setIsLogin(true);
        setPassword("");
      }
    } catch {
      setMessage("Cannot connect to server. Check Tomcat.");
    }
  }

  return (
    <div className="container">
      <form className="form" onSubmit={handleSubmit}>
        <h2>{isLogin ? "Login" : "Register"}</h2>

        {!isLogin && (
          <input
            placeholder="Full Name"
            value={name}
            onChange={e => setName(e.target.value)}
            required
          />
        )}

        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={e => setEmail(e.target.value)}
          required
        />

        <input
          type="password"
          placeholder="Password (minimum 8 characters)"
          value={password}
          onChange={e => setPassword(e.target.value)}
          minLength={8}
          maxLength={72}
          required
        />

        <button type="submit">
          {isLogin ? "Login" : "Register"}
        </button>

        <p>{message}</p>

        <p>
          {isLogin
            ? "Don't have an account?"
            : "Already have an account?"}
          {" "}
          <span
            className="link"
            onClick={() => {
              setIsLogin(!isLogin);
              setMessage("");
            }}
          >
            {isLogin ? "Register" : "Login"}
          </span>
        </p>
      </form>
    </div>
  );
}

export default App;
