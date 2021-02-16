import React from "react";
import { render, screen } from "@testing-library/react";

import NotificationBanner, { NotificationBannerHeading } from ".";

describe("NotificationBanner", () => {
  it("displays its title as a heading", () => {
    render(<NotificationBanner title="title-text">content</NotificationBanner>);

    expect(screen.getByRole("heading")).toHaveTextContent("title-text");
  });

  it("displays its contents", () => {
    render(
      <NotificationBanner title="">This is some content</NotificationBanner>
    );

    expect(screen.getByText("This is some content")).toBeVisible();
  });

  describe("accessibility", () => {
    it("grabs the user’s attention when displayed", () => {
      render(<NotificationBanner title="banner">content</NotificationBanner>);

      expect(screen.getByText("content").closest("[role=alert]")).toBeVisible();
    });

    it("is labelled by the title", () => {
      render(<NotificationBanner title="banner">content</NotificationBanner>);
      const container = screen.getByText("content").closest("[role=alert]");
      const title = screen.getByRole("heading", { name: "banner" });

      const id = container.getAttribute("aria-labelledby");

      expect(title).toHaveAttribute("id", id);
    });
  });

  describe("types", () => {
    it("defaults to the neutral type", () => {
      render(<NotificationBanner title="banner">content</NotificationBanner>);

      expect(
        screen.getByText("content").closest(".notification-banner--default")
      ).toBeVisible();
    });

    it("supports success type", () => {
      render(
        <NotificationBanner title="banner" type="success">
          content
        </NotificationBanner>
      );

      expect(
        screen.getByText("content").closest(".notification-banner--success")
      ).toBeVisible();
    });
  });

  describe("heading", () => {
    it("renders its contents", () => {
      render(<NotificationBannerHeading>content</NotificationBannerHeading>);

      expect(screen.getByText("content")).toBeVisible();
    });

    it("styles the heading", () => {
      render(<NotificationBannerHeading>content</NotificationBannerHeading>);

      expect(screen.getByText("content")).toHaveClass(
        "notification-banner-heading"
      );
    });
  });
});
