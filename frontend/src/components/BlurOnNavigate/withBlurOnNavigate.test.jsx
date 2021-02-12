import React from "react";
import user from "@testing-library/user-event";
import { render, screen } from "@testing-library/react";

import withBlurOnNavigate from "./withBlurOnNavigate";
import { Link, MemoryRouter } from "react-router-dom";

describe("withBlurOnNavigate", () => {
  const TestComponent = () => <div data-testid="test-component" />;

  const renderedComponent = () => screen.getByTestId("test-component");
  const focusableContainerOf = (element) => element.closest("*[tabIndex]");

  function setUp(component) {
    return render(
      <MemoryRouter initialEntries={["/"]}>{component}</MemoryRouter>
    );
  }

  it("returns a functional component", () => {
    const WrappedComponent = withBlurOnNavigate(TestComponent);
    setUp(<WrappedComponent />);

    expect(renderedComponent()).toBeInTheDocument();
  });

  it("wraps a component with a focusable container", () => {
    const WrappedComponent = withBlurOnNavigate(TestComponent);

    setUp(<WrappedComponent />);

    expect(focusableContainerOf(renderedComponent())).toBeInTheDocument();
  });

  it("connects focusable behaviour to the container", () => {
    const WrappedComponent = withBlurOnNavigate(TestComponent);

    setUp(
      <>
        <WrappedComponent />
        <Link to="/">link</Link>
      </>
    );
    user.click(screen.getByRole("link"));

    expect(focusableContainerOf(renderedComponent())).toHaveFocus();
  });

  it("passes props through to the wrapped component", () => {
    let receivedProps = null;
    const PropAwareTestComponent = (props) => {
      receivedProps = props;
      return <TestComponent />;
    };
    const WrappedComponent = withBlurOnNavigate(PropAwareTestComponent);

    setUp(<WrappedComponent name="value" />);

    expect(receivedProps).toEqual({
      name: "value",
    });
  });

  it("includes HoC in display name of wrapped component", () => {
    const WrappedComponent = withBlurOnNavigate(() => null);

    expect(WrappedComponent).toHaveProperty(
      "displayName",
      expect.stringMatching(/^WithBlurOnNavigate\(.+\)$/)
    );
  });
});
