
function createInput(props) {
    const {
        pattern = '',
        disable = '',
        min = '',
        max = '',
        classContainer = '',
        classLabel = '',
        classInput = '',
        type = 'text',
        name,
        value = '',
        require = false,
        classError = '',
        label,
        message,
    } = props;
    return `
        <div class="${classContainer}">
            <label class="${classLabel}">${label}</label>
            <input class="input-custom form-control ${classInput}" 
                type="${type}" name="${name}"
                min="${min}"
                max="${max}"
                ${pattern ? `pattern="${pattern}"` : ""}
                value="${value}"
                ${require ? 'required' : ''}
                ${disable}
            />
            <span class="error ${classError}">${message}</span>
        </div>
    `;
}

function createSelect(props) {
    const {
        classContainer = '',
        classLabel = '',
        classSelect = '',
        classError = '',
        name,
        pattern = '',
        value = '',
        type = 'text',
        require = false,
        id = '',
        multiple = false,
        label,
        message,
        options,
    } = props;

    const optionsStr = options.map(option => `<option value="${option.value}">${option.name}</option>`).join('');

    const selectElement = `
        <div class="${classContainer}">
            <label class="${classLabel}">${label}</label>
            <select class="input-custom form-control ${classSelect}" 
                name="${name}" 
                ${pattern ? `pattern="${pattern}"` : ""} 
                value="${value}"
                style="width: 100% !important"
                type="${type}"
                ${require ? 'required' : ''}
                id="${id}"
                ${multiple ? 'multiple="multiple"' : ''}
            >
                ${multiple ? '' : '<option value>---Choose---</option>'}
                ${optionsStr}
            </select>
            <span class="error ${classError}">${message}</span>
        </div>
    `;
    return selectElement;
}

function createFieldForm(props) {
    if (props.type === 'select') {
        return createSelect(props);
    }
    return createInput(props);
}


const onFocus = (formBody, index) => {
    const inputsForm = formBody.querySelectorAll('.input-custom')
    inputsForm[index].setAttribute("focused", "true"); // add 1 attribute focused=true.
}

function save() {

}

function renderForm(formBody, inputs, filmData) {
    formBody.innerHTML = ""; //clear ô input cũ
    inputs.forEach((input) => {
        formBody.innerHTML += createFieldForm(input); //gen từng ô input mới
    })
    const inputElements = formBody.querySelectorAll('.input-custom');

    // add sự kiện onFocus
    for (let i = 0; i < inputElements.length; i++) {
        inputElements[i].onblur = function () {
            onFocus(formBody, i)
            validateInput(inputs.at(i), inputElements[i], i)
        }
        inputElements[i].oninput = function () {
            validateInput(inputs.at(i), inputElements[i], i)

        }
    }
    if (filmData) {
        renderOptions(filmData);
    }

    $(document).ready(function () {
        $('.js-example-basic-single').select2({
            dropdownParent: $('#staticBackdrop')
        });
        $('.js-example-basic-multiple').select2({
            dropdownParent: $('#staticBackdrop')
        })
    })

}

document.addEventListener('invalid', (function () {
    return function (e) {
        e.preventDefault(); // chặn popup của html5
        e.target.onblur(); // call onblur của tất cả các ô input
    };
})(), true);

function validateInput(inputProp, inputElement, index) {
    const {validate, messageRequire, message, messageCustom} = inputProp;
    const error = document.getElementsByClassName('error')[index];
    const value = inputElement.value.trim();


    if (inputElement.required && value === '') {
        error.innerHTML = messageRequire || 'This field is required!';
        return;
    }
    if (!validate) return;
    const isValid = validate(value);
    error.innerHTML = isValid ? message : messageCustom;
}

function getDataFromForm(event, form) {
    event.preventDefault()
    const data = new FormData(form);
    return Object.fromEntries(data.entries())
}






