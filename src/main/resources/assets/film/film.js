const tBody = document.getElementById('tBody');
const ePagination = document.getElementById('pagination')
const eHeaderPublishDate = document.getElementById('header-publish-date')
const eSearch = document.getElementById('search');
const eFormBody = document.getElementById("formBody");
const eFormTitle = document.getElementById("staticBackdropLabel");
const eFormSubmitBtn = document.getElementById("form-submit-btn");
const eFilmForm = document.getElementById("filmForm");
let filmData;
let pageable = {
    page: 1,
    sort: 'id,desc',
    search: ''
}


async function createFilm() {
    const categories = $("#categories").select2('data').map(e => e.id);
    const actors = $('#actors').select2('data').map(e => e.id);
    const form = document.getElementById('filmForm');
    const formData = new FormData(form);
    let data = Object.fromEntries(formData.entries());

    data = {
        ...data,
        director: {id: data.director},
        categories,
        actors
    }

    const res = await fetch('/api/films', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    if (res.ok) {
        toastr.success("Created")
        await getList();
    } else if (res.status === 400) {
        toastr.warning("Bad Request")
    } else {
        toastr.warning("Something went wrong!")
    }
    const result = await res.json();
    $('#staticBackdrop').modal('hide');

}

eFilmForm.onsubmit = (e) => {
    e.preventDefault();
    eFormSubmitBtn.disabled = true;
    if (!filmData) {
        eFormTitle.innerText = "Create Film";
        eFormSubmitBtn.innerText = "Create";
        createFilm();
    } else {
        eFormTitle.innerText = "Save";
        eFormSubmitBtn.innerText = "Edit";
        editFilm();
    }
    setTimeout(() => {
        eFormSubmitBtn.disabled = false;
    }, 1000);
}

async function editFilm() {
    const categories = $("#categories").select2('data').map(e => e.id);
    const actors = $('#actors').select2('data').map(e => e.id);
    const form = document.getElementById('filmForm');
    const formData = new FormData(form);
    let data = Object.fromEntries(formData.entries());

    data = {
        ...data,
        director: {id: data.director},
        categories,
        actors
    }

    const res = await fetch(`/api/films/${data.id}`, { // Replace with your API endpoint for updating a film by ID
        method: 'PUT', // Use the appropriate HTTP method for updating (e.g., PUT or PATCH)
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    if (res.ok) {
        toastr.success("Updated"); // Display a success message
        await getList(); // Refresh the film list or perform any necessary actions
    } else {
        // Handle errors here (e.g., display an error message)
        toastr.error("Failed to update");
    }

    $('#staticBackdrop').modal('hide'); // Close the modal
}

function renderItemStr(item) {
    return `<tr>
                    <td>
                        ${item.id}
                    </td>
                    <td>
                        ${item.name}
                    </td>
                    <td>
                        ${item.director}
                    </td>
                    <td>
                        ${item.actors}
                    </td>
                    <td>
                        ${item.categories}
                    </td>
                    <td>
                        ${item.publishDate}
                    </td>
                    <td>
                        <button data-bs-toggle="modal" data-bs-target="#staticBackdrop" class="btn btn-primary btn-custom m-1 edit-button" data-film-id="${item.id}" onclick="showEdit(${item.id})"><i class="fa fa-edit"></i> </button>
                        <button class="btn btn-danger btn-custom m-1" data-film-id="${item.id}" onclick="return confirm('Are you sure?') && deleteFilm(${item.id})">
  <i class="fa fa-trash"></i>
</button>

                    </td>
                </tr>`
}


async function getList() {
    const response = await fetch(`/api/films?page=${pageable.page - 1 || 0}&sort=${pageable.sortCustom || 'id,desc'}&search=${pageable.search || ''}`);
    //response co ca status ok hay ko header {} body
    //{page: 1, size: 10, content: []}
    //{ size: 15, content: [1,2,3]}
    //{page:1 , size: 15, content: [1,2,3]}
    const result = await response.json();
    pageable = {
        ...pageable,
        ...result
    };
    genderPagination();
    renderTBody(result.content);
    //result tra ve data type map voi ben RestController
}

window.onload = () => {
    getList();
    onLoadSort();
    renderForm(eFormBody, resetInput());
}


const genderPagination = () => {
    ePagination.innerHTML = '';
    let str = '';

    const pagesToShow = 5; // Number of pages to display

    const startPage = Math.max(1, pageable.page - 2); // Calculate the start page
    const endPage = Math.min(pageable.totalPages, startPage + pagesToShow - 1); // Calculate the end page

    // Generate "Previous" button
    str += `<li class="page-item ${pageable.first ? 'disabled' : ''}">
              <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Previous</a>
            </li>`;

    // Generate page numbers
    for (let i = startPage; i <= endPage; i++) {
        str += `<li class="page-item ${(pageable.page) === i ? 'active' : ''}" aria-current="page">
                  <a class="page-link" href="#">${i}</a>
                </li>`;
    }

    // Generate "Next" button
    str += `<li class="page-item ${pageable.last ? 'disabled' : ''}">
              <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Next</a>
            </li>`;

    ePagination.innerHTML = str;

    const ePages = ePagination.querySelectorAll('li');
    const ePrevious = ePages[0];
    const eNext = ePages[ePages.length - 1];

    ePrevious.onclick = () => {
        if (pageable.page === 1) {
            return;
        }
        pageable.page -= 1;
        getList();
    };

    eNext.onclick = () => {
        if (pageable.page === pageable.totalPages) {
            return;
        }
        pageable.page += 1;
        getList();
    };

    for (let i = 1; i < ePages.length - 1; i++) {
        if (i === pageable.page) {
            continue;
        }
        ePages[i].onclick = () => {
            pageable.page = i;
            getList();
        };
    }
};


const onLoadSort = () => {
    eHeaderPublishDate.onclick = () => {
        let sort = 'publishDate,desc'
        if (pageable.sortCustom?.includes('publishDate') && pageable.sortCustom?.includes('desc')) {
            sort = 'publishDate,asc';
        }
        pageable.sortCustom = sort;
        getList();
    }
}
const onSearch = (e) => {
    e.preventDefault()
    pageable.search = eSearch.value;
    getList();
}

function renderOptions(filmData) {
    $('#director').val(filmData.director.value).trigger('change');
    $('#categories').val(filmData.categories.map(cate => cate.value)).trigger('change');
    $('#actors').val(filmData.actors.map(actor => actor.value)).trigger('change');

}


function resetInput(filmData) {
    let inputs;
    return inputs = [
        {
            label: "Name",
            name: "name",
            pattern: "^[A-Za-z ]{6,20}",
            message: "Username must have minimum is 6 charters and maximun is 20 charaters",
            require: true,
            value: filmData?.name || "",
            classContainer: "col-6",
            validate: (value) => {
                return value.length !== 0;
            },
            messageCustom: "...",
            messageRequire: "Name can't be blank"
        },
        {
            label: "id",
            name: "id",
            type: "",
            value: filmData?.id || "",
            classContainer: "d-none",

        },
        {
            label: "Publish Date",
            name: "publishDate",
            type: "date",
            min: "1900-01-01",
            max: "2023-09-05",
            require: true,
            value: filmData?.publishDate || "",
            message: "Please select Publish Date!",
            classContainer: "col-6",
            messageCustom: "Date from 1900-01-01 to 2023-09-05",

        },

        {
            label: "Director",
            name: "director",
            type: "select",
            message: "Please select director!",
            require: true,
            classSelect: "js-example-basic-single",
            id: "director",
            options: directors?.map(e => {
                return {
                    name: e.name,
                    value: e.value,
                }
            }),
        },
        {
            label: "Categories",
            name: "categories",
            type: "select",
            message: "Please select director!",
            require: true,
            classSelect: "js-example-basic-multiple",
            id: "categories",
            multiple: true,
            options: categories?.map(e => {
                return {
                    name: e.name,
                    value: e.value,
                }
            }),
        },
        {
            label: "Actors",
            name: "actors",
            type: "select",
            message: "Please select director!",
            require: true,
            classSelect: "js-example-basic-multiple",
            id: "actors",
            multiple: true,
            options: actors?.map(e => {
                return {
                    name: e.name,
                    value: e.value,
                }
            }),
        },

    ];

}

async function showEdit(id) {
    try {
        const response = await fetch(`/api/films/${id}`); // Replace with your API endpoint
        if (response.ok) {
            filmData = await response.json();
            renderForm(eFormBody, resetInput(filmData), filmData);
            // Open the modal or form for editing (you may have your own code for this)
        } else {
            // Handle error
        }
    } catch (error) {
        console.error(error);
    }
}

async function deleteFilm(id) {
    try {
        const response = await fetch(`/api/films/${id}`, {
            method: 'DELETE'
        });
        if (response.ok) {
            getList();
            toastr.success("Deleted");
        } else {
            toastr.error("Deleted fail");
        }
    } catch (error) {
        console.error(error);
    }
}





