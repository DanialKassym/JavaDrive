async function sendTextFile() {
  const fileInput = document.getElementById("fileInput");
  const file = fileInput.files[0];

  if (!file) {
    alert("Please select a valid .txt file.");
    return;
  }

  const formData = new FormData();
  formData.append("file", file);

  try {
    await fetch("http://localhost:8081/upload", {
      method: "POST",
      body: formData
    });

    console.log("File sent.");
  } catch (error) {
    console.error("Error sending file:", error);
  }

}


async function loginUser() {
  let password = document.querySelector('#passwordlog');
  let name = document.querySelector('#namelog');

  const data = {
    "Username": name.value,
    "Password": password.value
  };

  try {
    const response = await fetch("http://localhost:8081/auth/", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    });

    const token = response.headers.get("Authorization");

    if (token) {
      console.log("JWT:", token);
      localStorage.setItem("jwt", token.replace("Bearer ", ""));
      window.location.href = 'upload.html';
    } else {
      console.warn("JWT not found in response headers.");
    }
  } catch (error) {
    console.error("Error during registration:", error);
  }
}