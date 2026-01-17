async function registerUser() {
  let code = document.querySelector('#code');

  const data = {
    "Code": code.value
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