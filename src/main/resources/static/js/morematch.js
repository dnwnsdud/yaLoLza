  let count = 11;
  async function morematch() {
    let newPath = window.location.pathname.replace("/yalolza.gg/summoners/", ""); // 첫 번째 대체
    console.log(`https://yalolza.gg/more/${newPath}/${count}`);
    let text = await fetch(`/yalolza.gg/more/${newPath}/${count}`).then(r => r.text());
    count += 5;
    text = text.replace(/[^\\]*<body>/i, "").replace(/<\/body>[^\\]*/i, "");
    document.querySelector("div.user-right").innerHTML += text;
  }
  console.log("안녕나야");