let formDelete = document.forms["formDeleteUser"];
deleteUser();

async function deleteModalData(id) {
    const modal = new bootstrap.Modal(document.querySelector('#deleteModal'));
    await theModal(formDelete, modal, id);

    switch (formDelete.roles.value) {
        case '1':
            formDelete.roles.value = 'ROLE_ADMIN';
            break;
        case '2':
            formDelete.roles.value = 'ROLE_USER';
            break;
    }
}

function deleteUser() {
    formDelete.addEventListener("submit", ev => {
        ev.preventDefault();
        fetch("http://localhost:8080/admin/" + formDelete.id.value, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(() => {
            $('#deleteFormCloseButton').click();
            tableOfAllUsers();
        });
    });
}

function loadRolesDelete() {
    let select = document.getElementById("roleDelete");
    select.innerHTML = "";

    fetch("http://localhost:8080/admin/roles")
        .then(res => res.json())
        .then(data => {
            data.forEach(role => {
                let option = document.createElement("option");
                option.value = role.id;
                option.text = role.roleName;
                select.appendChild(option);
            });
        })
        .catch(error => console.error(error));
}

window.addEventListener("load", loadRolesDelete);


