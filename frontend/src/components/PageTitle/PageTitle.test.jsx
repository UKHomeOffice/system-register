import React from "react";
import { render } from "@testing-library/react";

import PageTitle from ".";

describe("PageTitle", () => {
  it("changes the title", () => {
    render(<PageTitle>The title</PageTitle>);

    expect(document.title).toEqual("The title — System Register");
  });
});
