const toTitle = (s) => {
    return s.replace("_", " ")
        .replace(/\w\S*/g, function (t) {
            return t.charAt(0).toUpperCase() + t.substr(1).toLowerCase();
        });
}

export default toTitle
