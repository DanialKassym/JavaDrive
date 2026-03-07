async function Login() {
  let username = document.querySelector('#username');
  let password = document.querySelector('#password');

  const data = {
    "username": username.value,
    "password": password.value
  };

  try {
    const response = await fetch("http://localhost:8081/login", {
      method: "POST",
      credentials: 'include',
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    });

    window.location.href = "home.html";
  } catch (error) {
    alert(error)
    console.error("Error during registration:", error);
  }
}