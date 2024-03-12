console.log("ello")

const nik = document.getElementById("nik");
const nama = document.getElementById("nama");
const hapus = document.getElementById("hapus");

nik.addEventListener("keyup", function(event){
    let value = nik.value;
    console.log(value);
    if(value.length >= 16){
        nik.value = value.substring(0,16);
    }
})

nama.addEventListener("keyup", function(event){
    let value = nama.value;
    nama.value = value.replace(/[^a-zA-Z0-9]+/, '');
})

hapus.addEventListener("click", function(event){
    confirm("are you sure delete this data ?");
})


