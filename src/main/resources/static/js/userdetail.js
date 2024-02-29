
const ctx = document.getElementById("myChart");

new Chart(ctx, {
  type: "doughnut",
  data: {
    datasets: [
      {
        data: [40, 60],
        borderWidth: 0,
        backgroundColor: ["#007aff", "#ff0558"],
      },
    ],
  },
  options: {
    plugins: {
      tooltip: { enabled: false },

      id: "myChart",
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