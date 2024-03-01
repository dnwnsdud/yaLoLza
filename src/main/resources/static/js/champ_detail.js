const swiper = new Swiper(".champskin", {
  slidesPerView: 4,
  spaceBetween: 10,
  navigation: {
    nextEl: ".swiper-button-next",
    prevEl: ".swiper-button-prev",
  },
});

function tooltip(
  currentTarget,
  tag,
  options = { gap: 5, backgroundColor: "black", borderRadius: "2px" },
  styleOptions = {}
) {
  if (typeof currentTarget === "string") {
    let targets = document.getElementsByClassName(currentTarget);
    for (let target of targets) {
      tooltip(target, tag, options, styleOptions);
    }
    return;
  }
  let gap = options.gap || 5;
  let getBound = (target) => {
    let data = target.getBoundingClientRect();
    return {
      left: data.left,
      right: data.right,
      top: data.top,
      bottom: data.bottom,
      width: data.right - data.left,
      height: data.bottom - data.top,
      center: {
        x: (data.right - data.left) / 2,
        y: (data.bottom - data.top) / 2,
      },
      realCenter: {
        x: data.left + (data.right - data.left) / 2,
        y: data.top + (data.bottom - data.top) / 2,
      },
    };
  };

  let [enter, leave] = [
    async (e) => {
      // enter
      currentTarget.removeEventListener("mouseenter", enter);
      currentTarget.addEventListener("mouseleave", leave);

      let div = document.createElement("div");
      let arrow = document.createElement("div");
      document.body.appendChild(div);
      div.setAttribute("tooltip-window-dependency", "");
      if (typeof tag === "string") div.innerHTML = tag;
      else if (typeof tag === "object" && tag instanceof Promise) {
        tag = await tag;
        if (typeof tag === "string") div.innerHTML = tag;
        else div.appendChild(tag);
      } else div.appendChild(tag);
      div.appendChild(arrow);
      div.style.backgroundColor = options.backgroundColor || "black";
      div.style.display = "inline-block";
      div.style.position = "absolute";
      div.style.minWidth = "16px";
      div.style.minHeight = "16px";
      div.style.overflow = "hidden";
      div.style.borderRadius = options.borderRadius || "0";
      for (let key in styleOptions) {
        div.style[key] = styleOptions[key];
      }
      arrow.style.backgroundColor = options.backgroundColor || "black";
      arrow.style.position = "absolute";
      arrow.style.display = "inline-block";
      arrow.style.width = "16px";
      arrow.style.height = "16px";
      arrow.style.transform = "rotate(45deg)";

      let [currRect, divRect, arrowRect] = [
        getBound(currentTarget),
        getBound(div),
        getBound(arrow),
      ];

      if (
        currRect.realCenter.y -
          currRect.height / 2 -
          gap -
          arrowRect.height / 2 -
          divRect.height >
        0
      ) {
        arrow.style.left = `${divRect.width / 2 - arrowRect.width / 2 + 3}px`;
        arrow.style.bottom = "-8px";
        div.style.left = `${currRect.realCenter.x - divRect.width / 2}px`;
        div.style.top = `${
          currRect.realCenter.y -
          divRect.height -
          arrowRect.height / 2 -
          gap -
          currRect.height / 2 +
          window.scrollY
        }px`;
      } else if (currRect.realCenter.y - divRect.height / 2 > 0) {
        if (
          currRect.realCenter.x -
            divRect.width -
            currRect.width / 2 -
            gap -
            arrowRect.width / 2 >
          0
        ) {
          arrow.style.right = `${-arrowRect.width / 2 + 3}px`;
          arrow.style.top = `${
            3 + divRect.center.y - arrowRect.height / 2 + window.scrollY
          }px`;
          div.style.left = `${
            currRect.realCenter.x -
            gap -
            arrowRect.width / 2 -
            currRect.width / 2 -
            divRect.width
          }px`;
          div.style.top = `${
            currRect.realCenter.y - divRect.height / 2 + window.scrollY
          }px`;
        } else {
          arrow.style.left = `${-arrowRect.width / 2 + 4}px`;
          arrow.style.top = `${
            3 + divRect.center.y - arrowRect.height / 2 + window.scrollY
          }px`;
          div.style.left = `${
            currRect.realCenter.x +
            gap +
            arrowRect.width / 2 +
            currRect.width / 2
          }px`;
          div.style.top = `${
            currRect.realCenter.y - divRect.height / 2 + window.scrollY
          }px`;
        }
      } else {
        arrow.style.left = `${divRect.width / 2 - arrowRect.width / 2 + 3}px`;
        arrow.style.top = `${-8 + window.scrollY}px`;
        div.style.left = `${currRect.realCenter.x - divRect.width / 2}px`;
        div.style.top = `${
          currRect.realCenter.y +
          currRect.height / 2 +
          gap +
          arrowRect.height / 2 +
          window.scrollY
        }px`;
      }
    },
    (e) => {
      currentTarget.addEventListener("mouseenter", enter);
      currentTarget.removeEventListener("mouseleave", leave);

      document.querySelector("[tooltip-window-dependency]").remove();
    },
  ];
  currentTarget.addEventListener("mouseenter", enter);
}
