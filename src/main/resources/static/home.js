const filesContainer = document.getElementById("files");
const statusBox = document.getElementById("status");
const searchInput = document.getElementById("searchInput");

let allFiles = [];
let currentFilter = "all";

/*
Expected backend response example:

GET /api/files

[
  {
    "id": 1,
    "name": "Homework.pdf",
    "type": "file"
  },
  {
    "id": 2,
    "name": "Photos",
    "type": "folder"
  }
]
*/

async function fetchFiles() {
   // statusBox.textContent = "Loading...";

    try {
        const response = await fetch("http://localhost:8081/dashboard", {
            method: "GET",
            credentials: "include"
        });

        if (!response.ok) {
            throw new Error("Server error: " + response.status);
        }

        const data = await response.json();

        allFiles = data;
        //renderFiles();

        //statusBox.textContent = "";

    } catch (error) {
        //statusBox.textContent = "Failed to load files.";
        console.error(error);
    }
}

function renderFiles() {
    filesContainer.innerHTML = "";

    let filtered = allFiles.filter(file => {
        if (currentFilter === "all") return true;
        return file.type === currentFilter;
    });

    let search = searchInput.value.toLowerCase();

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
        // placeholder for folder navigation
        console.log("Open folder:", file.id);
    } else {
        // browser-native rendering
        window.open("/api/files/" + file.id, "_blank");
    }
}

function filterFiles(type) {
    currentFilter = type;
    renderFiles();
}


/* Events */

//document.getElementById("newBtn").onclick = addFile;

document.querySelectorAll(".menu div").forEach(item => {
    item.onclick = () => {
        currentFilter = item.dataset.filter;
        renderFiles();
    };
});

//searchInput.addEventListener("input", renderFiles);

/* Init */

document.addEventListener('DOMContentLoaded', fetchFiles());