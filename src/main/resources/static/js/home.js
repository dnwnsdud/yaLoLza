const swiper = new Swiper(".homeswiper", {
  slidesPerView: "auto",
  // autoHeight: true,
  centeredSlides: true,
  autoplay: {
    delay: "5000",
    disableOnInteraction: false,
  },
  slideToClickedSlide : true,
  spaceBetween: 30,
  rewind: true,
  navigation: {
    nextEl: ".next1",
    prevEl: ".prev1",
  },
});
