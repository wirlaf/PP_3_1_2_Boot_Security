async function theModal(form, modal, id){
    modal.show();
    let user = await getUserId(id);
    form.id.value = user.id;
    form.firstName.value = user.firstName;
    form.secondName.value = user.secondName;
    form.age.value = user.age;
    form.username.value = user.username;
    form.roles.value = user.roles;
}