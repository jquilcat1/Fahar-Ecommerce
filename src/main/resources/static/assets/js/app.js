document.addEventListener("DOMContentLoaded", function () {
  console.log("FAHAR Streetwear - Sistema Front-End Iniciado");

  // Validación de formularios de registro
  const registroForm = document.getElementById("registroForm");
  if (registroForm) {
    const inputs = registroForm.querySelectorAll("input");
    inputs.forEach((input) => {
      input.addEventListener("input", function () {
        if (input.checkValidity()) {
          input.classList.remove("is-invalid");
          input.classList.add("is-valid");
        } else {
          input.classList.remove("is-valid");
          input.classList.add("is-invalid");
        }
      });
    });

    registroForm.addEventListener(
      "submit",
      function (event) {
        if (!registroForm.checkValidity()) {
          event.preventDefault();
          event.stopPropagation();
        }
        registroForm.classList.add("was-validated");
      },
      false,
    );
  }

  // Control de entrada para el campo de teléfono
  const inputTelefono = document.getElementById("inputTelefono");
  if (inputTelefono) {
    if (inputTelefono.value === "") {
      inputTelefono.value = "9";
    }
    inputTelefono.addEventListener("input", function (e) {
      this.value = this.value.replace(/\D/g, "");
      if (!this.value.startsWith("9")) {
        this.value = "9" + this.value;
      }
    });
  }

  // Interacción para mostrar/ocultar contraseña en el formulario de registro
  const togglePassword = document.getElementById("togglePassword");
  const inputPassword = document.getElementById("inputPassword");
  if (togglePassword && inputPassword) {
    togglePassword.addEventListener("click", function () {
      const tipoActual = inputPassword.getAttribute("type");
      if (tipoActual === "password") {
        inputPassword.setAttribute("type", "text");
        this.classList.remove("bi-eye");
        this.classList.add("bi-eye-slash");
      } else {
        inputPassword.setAttribute("type", "password");
        this.classList.remove("bi-eye-slash");
        this.classList.add("bi-eye");
      }
    });
  }

  // Interacción para mostrar/ocultar contraseña en el formulario de login
  const toggleLoginPassword = document.getElementById("toggleLoginPassword");
  const loginPassword = document.getElementById("loginPassword");
  if (toggleLoginPassword && loginPassword) {
    toggleLoginPassword.addEventListener("click", function () {
      const tipo =
        loginPassword.getAttribute("type") === "password" ? "text" : "password";
      loginPassword.setAttribute("type", tipo);
      this.classList.toggle("bi-eye");
      this.classList.toggle("bi-eye-slash");
    });
  }

  // Libro de reclamaciones: Mostrar alerta si el asunto es "reclamo"
  const selectAsunto = document.getElementById("asunto");
  const alertaReclamo = document.getElementById("alertaReclamo");

  if (selectAsunto && alertaReclamo) {
    // Verificar al cargar la página (por si viene de la URL ?asunto=reclamo)
    if (selectAsunto.value === "reclamo") {
      alertaReclamo.classList.remove("d-none");
    }

    // Escuchar si el usuario cambia la opción manualmente
    selectAsunto.addEventListener("change", function () {
      if (this.value === "reclamo") {
        alertaReclamo.classList.remove("d-none");
      } else {
        alertaReclamo.classList.add("d-none");
      }
    });
  }

  // Validación de formulario de contacto
  const contactoForm = document.getElementById("contactoForm");
  if (contactoForm) {
    // 1. Excluimos 'select' de la validación verde en tiempo real
    const inputsContacto = contactoForm.querySelectorAll("input, textarea");

    inputsContacto.forEach((input) => {
      input.addEventListener("input", function () {
        if (input.checkValidity()) {
          input.classList.remove("is-invalid");
          input.classList.add("is-valid");
        } else {
          input.classList.remove("is-valid");
          input.classList.add("is-invalid");
        }
      });
    });

    // 2. El select de Asunto SOLO se pinta de rojo si hay error, nunca de verde
    const selectAsunto = document.getElementById("asunto");
    if (selectAsunto) {
      selectAsunto.addEventListener("change", function () {
        if (this.checkValidity()) {
          this.classList.remove("is-invalid");
          // Omitimos intencionalmente el add('is-valid')
        } else {
          this.classList.add("is-invalid");
        }
      });
    }

    // 3. Bloqueo de envío
    contactoForm.addEventListener(
      "submit",
      function (event) {
        if (!contactoForm.checkValidity()) {
          event.preventDefault();
          event.stopPropagation();
        }
        contactoForm.classList.add("was-validated");
      },
      false,
    );
  }
});
