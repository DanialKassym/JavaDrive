const filesContainer = document.getElementById("files");
const statusBox = document.getElementById("status");
const searchInput = document.getElementById("searchInput");

let allFiles = [];
let currentFilter = "all";


async function fetchFiles() {
    try {
        const response = await fetch("/api/v1/files/dashboard", {
            method: "GET",
            credentials: "include"
        });

        if (!response.ok) {
            throw new Error("Server error: " + response.status);
        }
         const rawText = await response.text();

         const data = JSON.parse(rawText);

        allFiles = data.map(file => ({
            id: file.id,
            name: file.original || "Unnamed File",
            type: file.type || "file"
        }));

        renderFiles();

    } catch (error) {
        console.error("Dashboard load failed:", error);
    }
}

function renderFiles() {
    filesContainer.innerHTML = "";

    let filtered = allFiles.filter(file => {
        if (currentFilter === "all") return true;
        return file.type === currentFilter;
    });

    let search = searchInput ? searchInput.value.toLowerCase() : "";
    filtered = filtered.filter(file =>
        file.name.toLowerCase().includes(search)
    );

    if (filtered.length === 0) {
        filesContainer.innerHTML = "<p>No files found</p>";
        return;
    }

    filtered.forEach(file => {
        const div = document.createElement("div");
        div.className = "file";

        div.innerHTML = `
            <div>${file.type === "folder" ? "📁" : "📄"}</div>
            <div>${file.name}</div>
        `;

        div.onclick = () => openFile(file);
        filesContainer.appendChild(div);
    });
}

function openFile(file) {
    if (file.type === "folder") {
        console.log("Open folder:", file.id);
    } else {
        window.open("/api/v1/files/" + file.id, "_blank");
    }
}

if (searchInput) {
    searchInput.addEventListener("input", renderFiles);
}

document.addEventListener('DOMContentLoaded', fetchFiles);
