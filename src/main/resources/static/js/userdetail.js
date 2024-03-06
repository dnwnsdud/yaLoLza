
const ctx = document.getElementById("myChart");

const s  = document.getElementById("siwwin");
const w  = document.getElementById("sjwtotal");
const sValue = parseInt(s.textContent || s.innerText, 10);
const wValue = parseInt(w.textContent || w.innerText, 10);

console.log("안녕 차트야 ");
new Chart(ctx, {
  type: "doughnut",
  data: {
    datasets: [
      {
        data: [wValue,sValue ],
        borderWidth: 0,
        backgroundColor: ["#ff0558", "#007aff"],
      },
    ],
  },
  options: {
    plugins: {
      tooltip: { enabled: false },
      title: "myChart", // 수정된 부분: "id" 대신 "title"
    },
  },
});

let btn = document.querySelectorAll(".usergame-btn > button");

btn.forEach((bt) => {
  bt.addEventListener("click", (e) => {
	  console.log("클릭")
    let target =
      e.currentTarget.parentElement.parentElement.parentElement
        .nextElementSibling;
    if (!target.style.display || target.style.display == "none")
      target.style.display = "block";
    else target.style.display = "none";
  });
});

let all = document.querySelectorAll(".buildbtn-all");
let btnall = document.querySelectorAll(".btn-all");
let build = document.querySelectorAll(".buildbtn-build");
let btnbuild = document.querySelectorAll(".btn-build");

btnall.forEach((e, index) => {
  e.addEventListener("click", () => {
    all[index].classList.add("on");
    btnall[index].classList.add("on");
    build[index].classList.remove("on");
    btnbuild[index].classList.remove("on");
  });
});
btnbuild.forEach((e, index) => {
  e.addEventListener("click", () => {
    build[index].classList.add("on");
    btnbuild[index].classList.add("on");
    all[index].classList.remove("on");
    btnall[index].classList.remove("on");
  });
});

let totalchampions =document.querySelector(".user-totalchampions");
let totalchampionsbutton =document.querySelector(".totalchampionsbutton");
let userleft =document.querySelector(".user-left");
let userright =document.querySelector(".user-right");

function champions(){
	totalchampionsbutton.style.backgroundColor = "var(--point7)";
    totalchampionsbutton.style.borderRadius = "0.5rem";
    totalchampions.style.display = "block";
    userleft.style.display = "none";
    userright.style.display = "none";
}

function allresult(){
	totalchampionsbutton.style.backgroundColor = "var(--point8)";
    totalchampionsbutton.style.borderRadius = "none";
    totalchampions.style.display = "none";
    userleft.style.display = "block";
    userright.style.display = "block";
}