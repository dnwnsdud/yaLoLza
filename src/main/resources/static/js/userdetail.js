let count = 11; 

function champions() {
    let totalchampionsbutton = document.querySelector(".totalchampionsbutton");
    let totalchampions = document.querySelector(".user-totalchampions");
    let userleft = document.querySelector(".user-left");
    let userright = document.querySelector(".user-right");

    totalchampionsbutton.style.borderRadius = "0.5rem";
    totalchampions.style.display = "block";
    userleft.style.display = "none";
    userright.style.display = "none";
}

function allresult() {
    let totalchampionsbutton = document.querySelector(".totalchampionsbutton");
    let totalchampions = document.querySelector(".user-totalchampions");
    let userleft = document.querySelector(".user-left");
    let userright = document.querySelector(".user-right");

    totalchampionsbutton.style.borderRadius = "none";
    totalchampions.style.display = "none";
    userleft.style.display = "block";
    userright.style.display = "block";
}

function addEventListenersToButtons() {
    let btn = document.querySelectorAll(".usergame-btn > button");
    btn.forEach((bt) => {
        bt.addEventListener("click", (e) => {
            console.log("클릭");
            let target =
                e.currentTarget.parentElement.parentElement.parentElement
                .nextElementSibling;
            if (!target.style.display || target.style.display == "none")
                target.style.display = "block";
            else
                target.style.display = "none";
        });
    });

    let btnall = document.querySelectorAll(".btn-all");
    let build = document.querySelectorAll(".buildbtn-build");
    let btnbuild = document.querySelectorAll(".btn-build");

    btnall.forEach((e, index) => {
        e.addEventListener("click", () => {
            let all = document.querySelectorAll(".buildbtn-all");
            all[index].classList.add("on");
            e.classList.add("on");
            build[index].classList.remove("on");
            btnbuild[index].classList.remove("on");
        });
    });

    btnbuild.forEach((e, index) => {
        e.addEventListener("click", () => {
            let all = document.querySelectorAll(".buildbtn-all");
            build[index].classList.add("on");
            e.classList.add("on");
            all[index].classList.remove("on");
            let btnall = document.querySelectorAll(".btn-all");
            btnall[index].classList.remove("on");
        });
    });
}

function chartt() {
    const ctx = document.getElementById("myChart");
    console.log("차트야나왔어");
    const s = document.getElementById("siwwin");
    const w = document.getElementById("sjwtotal");
    const sValue = parseInt(s.textContent || s.innerText, 10);
    const wValue = parseInt(w.textContent || w.innerText, 10);

    console.log("안녕 차트야 ");

    new Chart(ctx, {
        type: "doughnut",
        data: {
            datasets: [{
                data: [wValue, sValue],
                borderWidth: 0,
                backgroundColor: ["#ff0558", "#007aff"],
            }, ],
        },
        options: {
            plugins: {
                tooltip: { enabled: false },
                title: "myChart",
            },
        },
    });
}

document.addEventListener('DOMContentLoaded', function() {
    addEventListenersToButtons();
    chartt();
    champions();
    allresult();
});

document.querySelector(".totalchampionsbutton").addEventListener("click", champions);
document.querySelector(".allresultbutton").addEventListener("click", allresult);


async function morematch() {
    let newPath = window.location.pathname.replace("/yalolza.gg/summoners/", "");
    console.log(`https://yalolza.gg/more/${newPath}/${count}`);
    let text = await fetch(`/yalolza.gg/more/${newPath}/${count}`).then(r => r.text());    
    count += 5;
    text = text.replace(/[^\\]*<body>/i, "").replace(/<\/body>[^\\]*/i, "");

    document.querySelector("div.donghuck").innerHTML += text;
    chartt();
    addEventListenersToButtons();
}