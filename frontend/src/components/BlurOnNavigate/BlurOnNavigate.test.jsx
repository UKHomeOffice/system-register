import React from "react";
import user from "@testing-library/user-event";
import { Link, MemoryRouter, Route } from "react-router-dom";
import { render, screen, waitFor } from "@testing-library/react";

import { BlurOnNavigate } from ".";

describe("BlurOnNavigate", () => {
  const container = React.createRef();

  function setUp(component) {
    return render(
      <MemoryRouter initialEntries={["/"]}>
        <Route path="/">text</Route>
        {component}
      </MemoryRouter>
    );
  }

  const linkElement = () => screen.getByRole("link");

  it("removes focus after navigating to a different path", async () => {
    setUp(
      <div ref={container} tabIndex={-1}>
        <BlurOnNavigate container={container} />
        <Link to="/path">link</Link>
      </div>
    );

    user.click(linkElement());

    await waitFor(() => {
      expect(linkElement()).not.toHaveFocus();
    });
  });

  it("removes focus after navigating to the same path", async () => {
    setUp(
      <div ref={container} tabIndex={-1}>
        <BlurOnNavigate container={container} />
        <Link to="/">link</Link>
      </div>
    );

    user.click(linkElement());

    await waitFor(() => {
      expect(linkElement()).not.toHaveFocus();
    });
  });

  it("does not change focus if only the fragment changed", async () => {
    setUp(
      <div ref={container} tabIndex={-1}>
        <BlurOnNavigate container={container} />
        <Link to="/#fragment">link</Link>
      </div>
    );

    user.click(linkElement());

    await waitFor(() => {
      expect(linkElement()).toHaveFocus();
    });
  });

  it("does change focus if the path and fragment changed", async () => {
    setUp(
      <div ref={container} tabIndex={-1}>
        <BlurOnNavigate container={container} />
        <Link to="/path#fragment">link</Link>
      </div>
    );

    user.click(linkElement());

    await waitFor(() => {
      expect(linkElement()).not.toHaveFocus();
    });
  });
});
