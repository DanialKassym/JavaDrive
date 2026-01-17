async function registerUser() {
  let email = document.querySelector('#email');

  const data = {
    "Email": email.value
  };

  try {
    const response = await fetch("http://localhost:8081/register/", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    });
  } catch (error) {
    console.error("Error during registration:", error);
  }
  finally{
    window.location.href = "emailconf.html";
  }
}