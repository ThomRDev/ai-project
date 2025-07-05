const fileInput = document.getElementById("fileInput");
const fileNameDisplay = document.getElementById("fileNameDisplay");
const dropContainer = document.getElementById("dropContainer");
const iconDrop = document.getElementById("icon-drop")
const titleDrop = document.getElementById("title-drop")

fileInput?.addEventListener("change", (event) => {
   const file = event.target.files[0];
   if (file) {
       fileNameDisplay.textContent = `Archivo seleccionado: ${file.name}`;
   } else {
       fileNameDisplay.textContent = "";
   }
});

dropContainer.addEventListener('dragover', (e) => {
  e.preventDefault();
  dropContainer.classList.remove('border-gray-400/50')
  iconDrop.classList.remove('text-gray-400/50')
  titleDrop.classList.remove('text-gray-700')

  dropContainer.classList.add('border-[#2B6CFC]');
  iconDrop.classList.add('text-[#C0D4FF]');
  titleDrop.classList.add('text-[#2B6CFC]');
});

dropContainer.addEventListener('dragleave', (e) => {
  e.preventDefault();

  dropContainer.classList.remove('border-[#2B6CFC]');
  iconDrop.classList.remove('text-[#C0D4FF]');
  titleDrop.classList.remove('text-[#2B6CFC]');

  dropContainer.classList.add('border-gray-400/50')
  iconDrop.classList.add('text-gray-400/50')
  titleDrop.classList.add('text-gray-700')
});

dropContainer.addEventListener('drop', (e) => {
    e.preventDefault()

  const files = e.dataTransfer.files;
      if (files.length > 0) {
        const droppedFile = files[0];

        const dataTransfer = new DataTransfer();
        dataTransfer.items.add(droppedFile);
        fileInput.files = dataTransfer.files;

        const changeEvent = new Event('change', { bubbles: true });
        fileInput.dispatchEvent(changeEvent);

      }
 })

document.getElementById("form")?.addEventListener("submit", event => {
  event.preventDefault();

  const file = fileInput.files[0];

  if (file) {
    const formData = new FormData();
    formData.append("videoFile", file);

    fetch("/upload", {
      method: "POST",
      body: formData,
      credentials: "include",
    headers: {
      "X-Requested-With": "XMLHttpRequest"
    }
    })
    .then(response => {
      if (!response.ok) throw new Error("Error en el upload");
      return response.text();
    })
    .then(data => {
         const parser = new DOMParser();
         const doc = parser.parseFromString(data, "text/html");
         const loginModal = doc.querySelector("#modal-container");
         doc.querySelector("#login-modal")?.classList.remove("bg-gray-900")
         doc.querySelector("#login-modal")?.classList.add("bg-gray-900/70")
         if (loginModal) {
           const container = document.getElementById("modal");
           container.innerHTML = "";
           container.appendChild(loginModal);
         }
    })
    .catch(error => {
      console.error("Error al subir el archivo:", error);
    });
  } else {
    console.log("No hay archivo seleccionado");
  }
});