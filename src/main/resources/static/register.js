async function SendEmail() {
  let email = document.querySelector('#email');

  const data = {
    "Email": email.value
  };

  try {
    const response = await fetch("http://localhost:8081/emailConfirmation", {
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