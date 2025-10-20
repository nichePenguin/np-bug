document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('browse').addEventListener('click', browse);
  document.getElementById('submit-button').addEventListener('click', handleSubmit);
  document.getElementById('file-fake').addEventListener('input', handleValidation);
  document.getElementById('file').addEventListener('input', handleValidation);
  document.getElementById('name').addEventListener('input', handleValidation);
  document.getElementById('latin-name').addEventListener('input', handleLatinNameInput);
  handleLatinNameInput(null)
  handleValidation(null)
});

var has_latin_name = false
var file = null

function handleValidation(event) {
  const hasImage = document.getElementById('file-fake').value.trim();
  const hasName = document.getElementById('name').value.trim();
  const submit = document.getElementById('submit-button')
  if (hasImage && hasName) {
    submit.disabled = false
    setError(null)
    } else {
    submit.disabled = true
    if (!hasImage && !hasName) {
      setError("Image and name are required")
    } else if (hasImage) {
      setError("Name is required")
    } else {
      setError("Image is required")
    }
  }
}

function handleLatinNameInput(event) {
  const text = document.getElementById('latin-name').value.trim()
  let confidence = document.getElementById('confidence')
  let hint = document.getElementById('hint')
  if (text.length === 0) {
    has_latin_name = false
    hint.style.display = "block"
    confidence.style.display = "none"
  } else {
    has_latin_name = true
    hint.style.display = "none"
    confidence.style.display = "block"
  }
}

var errorPresent = false;
var errorTempFade = null

function errorFade(div, text) {
  errorPresent = false
  div.style.opacity = 0;
  setTimeout(() => {
    div.style.display = "none"
  }, 500);
  text.innerHTML = ""

}

function setError(err, temp){
  if (errorTempFade) {
    clearTimeout(errorTempFade)
  }

  const errorDiv = document.getElementById('error')
  const errorText = document.getElementById('error-text')
  if (err) {
    if (!errorPresent) {
      errorPresent = true
      errorDiv.style.display = "block"
      errorDiv.style.opacity = 0;
      setTimeout(() => {
        errorDiv.style.opacity = 1;
      }, 1);
    }
    errorText.innerHTML = err
    console.log(err)
  } else {
    errorFade(errorDiv, errorText)
  }
  if (temp) {
    errorTempFade = setTimeout(() => {
      errorFade(errorDiv, errorText)
      errorTempFade = null
    }, 5000);
  }
}

function browse() {
  const fileInput = document.getElementById("file");
  const fileFake = document.getElementById('file-fake');
  if (!fileInput.hasAttribute('listener')) {
    fileInput.addEventListener('change', ((source) => {
      source.currentTarget.setAttribute('listener', true);
      fileFake.value = fileInput.files[0].name;
      file = fileInput.files[0]
      handleValidation(null)
    }));
  }
  fileInput.click();
}

function val(id) {
  return document.getElementById(id).value.trim()
}

function confidence() {
  return parseInt(document.querySelector('input[name="confidence"]:checked').value)
}

function detail() {
  return document.querySelector('input[name="detail"]:checked').value
}

function anonymous() {
  return document.getElementById('anonymous-cb').checked
}

function handleSubmit(event) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append("name", val('name'));
  formData.append("latin", val('latin-name'));
  formData.append("hint", val('hint-text'));
  formData.append("confidence", confidence());
  formData.append("detail", detail());
  formData.append("anonymous", anonymous());

  startLoading()

  fetch("api/submit", {
    method: 'POST',
    body: formData,
    credentials: 'same-origin',
  }).then(response => {
    const contentType = response.headers.get("content-type");
    if (contentType) {
      if (contentType == "application/json") {
        return response.json();
      } else if (contentType == "text/plain") {
        response.text().then(setError);
      } else {
        console.log("Error output is not in text/plain:");
        response.text().then(console.log);
      }
    }
    setError("Failed to submit: see console log", true)

    stopLoading();

    Promise.reject('Failed to submit');
  })
  .then(load)
  .catch(e => {
    setError(e, true)
    stopLoading()
  });
}

function load(json){
  stopLoading();

  if (!json) {
    return
  }

  if (!json.success) {
    setError(json.error, true)
  } else {
    let success = document.getElementById('success')
    success.style.opacity = 1
    success.style.display = "inline-block"
    setTimeout(() => {
      success.style.opacity = 0
    }, 5000)
    setTimeout(() => {
      success.style.display = "none"
    }, 5500)
  }
}

function startLoading() {
  document.body.classList.add('waiting');
  document.getElementById('submit-button').disabled = true;
  for (let e of document.getElementsByClassName('bug-loading')) {
    e.style.opacity = 1
  }
}

function stopLoading() {
  document.body.classList.remove('waiting');
  document.getElementById('submit-button').disabled = false;
  for (let e of document.getElementsByClassName('bug-loading')) {
    e.style.opacity = 0
  }
}
