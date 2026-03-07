const fileInput = document.getElementById("fileInput");
const uploadBtn = document.getElementById("uploadBtn");
const statusBox = document.getElementById("status");
const fileNameBox = document.getElementById("fileName");

let selectedFile = null;

fileInput.addEventListener("change", () => {
    selectedFile = fileInput.files[0];

    if (selectedFile) {
        fileNameBox.textContent = selectedFile.name;
        statusBox.textContent = "";
    } else {
        fileNameBox.textContent = "No file selected";
    }
});

uploadBtn.addEventListener("click", async () => {
    console.log(fileInput.files[0]);
    console.log(fileInput.files[0]?.size);
    const selectedFile = fileInput.files[0];

    if (!selectedFile) {
        statusBox.textContent = "Please select a file first.";
        return;
    }

    statusBox.textContent = "Uploading...";

    const formData = new FormData();
    formData.append("file", selectedFile);

    try {
        const response = await fetch("http://localhost:8081/upload", {
            method: "POST",
            body: formData,
            credentials: "include"
        });

        if (!response.ok) {
            throw new Error("Upload failed");
        }

        window.location.href = "home.html";

    } catch (error) {
        console.error(error);
        statusBox.textContent = "Upload failed. Try again.";
    }
});

function goBack() {
    window.location.href = "home.html";
}