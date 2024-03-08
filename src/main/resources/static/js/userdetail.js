let count = 11;

let datas = [
  {
    tooltip: ".usergame-flex1 div",
    link: "/info/summonerinfo/",
  },
  {
    tooltip: ".usergame-flex2 div",
    link: "/info/runedetailinfo/",
  },
  {
    tooltip: ".usergame-id div",
    link: "/info/championNameinfo/",
  },
  {
    tooltip: ".usergame-items li",
    link: "/info/itemdetailinfo/",
  },
  {
    tooltip: ".vic-team li .champion >div",
    link: "/info/championNameinfo/",
  },
  {
    tooltip: ".vic-spell .spell >div",
    link: "/info/summonerinfo/",
  },
  {
    tooltip: ".vic-spell .rune >div",
    link: "/info/runedetailinfo/",
  },
  {
    tooltip: ".victotal-items div",
    link: "/info/itemdetailinfo/",
  },
  {
    tooltip: ".usergame-objects li div",
    link: "/info/objectinfo/",
  },
  {
    tooltip: ".itembuild-group div .img-wrap1",
    link: "/info/itemdetailinfo/",
  },
  {
    tooltip: ".skillbuild-grid .img-wrap3",
    link: "/info/spell/",
  },
  {
    tooltip: ".userskill-qwe .img-wrap3",
    link: "/info/spell/",
  },
  {
    tooltip: ".main-rune ul li",
    link: "/info/runedetailinfo/",
  },
  {
    tooltip: ".sub-rune ul .img-wrap",
    link: "/info/runedetailinfo/",
  },
  {
    tooltip: ".fragments-rune ul .img-wrap",
    link: "/info/perkdetailinfo/",
  },
];

function champions() {
    // 챔피언 버튼 활성화
    let totalchampionsbutton = document.querySelector(".totalchampionsbutton");
    let totalchampions = document.querySelector(".user-totalchampions");
    let userleft = document.querySelector(".user-left");
    let userright = document.querySelector(".user-right");

    totalchampionsbutton.style.backgroundColor = "var(--point7)";
    totalchampionsbutton.style.borderRadius = "0.5rem";
    totalchampions.style.display = "block";
    userleft.style.display = "none";
    userright.style.display = "none";
}

function allresult() {
    // 전체 결과 표시
    let totalchampionsbutton = document.querySelector(".totalchampionsbutton");
    let totalchampions = document.querySelector(".user-totalchampions");
    let userleft = document.querySelector(".user-left");
    let userright = document.querySelector(".user-right");

    totalchampionsbutton.style.backgroundColor = "var(--point8)";
    totalchampionsbutton.style.borderRadius = "none";
    totalchampions.style.display = "none";
    userleft.style.display = "block";
    userright.style.display = "block";
}

function addEventListenersToButtons() {
    // 버튼에 이벤트 리스너 추가
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

    // 버튼 클릭 시 클래스 토글
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
    // 차트 생성
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

// 페이지 로드 시 초기 설정
document.addEventListener('DOMContentLoaded', function() {
    addEventListenersToButtons();
    chartt();
    champions();
    allresult();
    generateTooltips();
    tooltip();
});

// 챔피언 버튼 클릭 이벤트
document.querySelector(".totalchampionsbutton").addEventListener("click", champions);
// 전체 결과 버튼 클릭 이벤트
document.querySelector(".allresultbutton").addEventListener("click", allresult);


function tooltipGenerator(
    tooltipSelector,
    fetchLink,
    color = "#48C4B7",
    width = "250px"
) {
    // 툴팁 생성 함수
    document.querySelectorAll(tooltipSelector).forEach(async (element) => {
        if (element.id == 0) return;
        let target = await fetch(fetchLink + element.id).then((data) =>
            data.text()
        );
        tooltip(
            element,
            target,
            {
                gap: 20,
                backgroundColor: "var(--gray2)",
                borderRadius: "1rem",
            },
            {
                fontSize: "15px",
                color: color,
                width: width,
                padding: "10px",
            }
        );
    });
}


// 툴팁 생성 함수 호출
async function generateTooltips() {
    for (let index in datas) {
        await new Promise(resolve => {
            setTimeout(() => {
                tooltipGenerator(
                    datas[index].tooltip,
                    datas[index].link,
                    datas[index].color ? target.color : "#48C4B7",
                    datas[index].width ? target.width : "250px"
                );
                resolve();
            }, index * 500);
        });
    }
}

// 추가 경기 정보 로드 함수
async function morematch() {
    let newPath = window.location.pathname.replace("/yalolza.gg/summoners/", "");
    console.log(`https://yalolza.gg/more/${newPath}/${count}`);
    let text = await fetch(`/yalolza.gg/more/${newPath}/${count}`).then(r => r.text());
    count += 5;
    text = text.replace(/[^\\]*<body>/i, "").replace(/<\/body>[^\\]*/i, "");

    document.querySelector("div.user-right").innerHTML += text;
    chartt();
    generateTooltips();
    addEventListenersToButtons();
    tooltip();
}
