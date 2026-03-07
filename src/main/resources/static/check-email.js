async function SendEmail() {
  let email = document.querySelector('#email');

  const data = {
    "email": email.value
  };

  try {
    const response = await fetch("http://localhost:8081/emailConfirmation", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    });

    window.location.href = "email-verification.html";
  } catch (error) {
    alert(error)
    console.error("Error during registration:", error);
  }
}