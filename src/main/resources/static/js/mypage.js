const modal = document.querySelector(".modal");
const btnOpenModal = document.querySelector(".btn-open-modal");
const close = document.querySelector(".close");
const close1 = document.querySelector(".cancel");

btnOpenModal.addEventListener("click", () => {
  modal.style.display = "flex";
});
close.addEventListener("click", () => {
  modal.style.display = "none";
});
close1.addEventListener("click", () => {
  modal.style.display = "none";
});

function cancelFunction() {
  alert("취소 버튼이 눌렸습니다!");
}

const modal2 = document.querySelector(".modal2");
const btnOpenModal2 = document.querySelector(".btn-open-modal2");
const close2 = document.querySelector(".close2");
const close12 = document.querySelector(".cancel2");
btnOpenModal2.addEventListener("click", () => {
  modal2.style.display = "flex";
});
close2.addEventListener("click", () => {
  modal2.style.display = "none";
});
close12.addEventListener("click", () => {
  modal2.style.display = "none";
});
