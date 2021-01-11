import { rest } from "msw";
import { setupServer } from "msw/node";

import ValidationError from "../validationError";
import api from '../api'
import data from '../../data/systems_dummy.json';

const server = setupServer(
  rest.get("/api/systems", (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json(data)
    );
  })
);

describe("api", () => {
  beforeAll(() => server.listen({ onUnhandledRequest: "error" }));

  afterAll(() => server.close());

  afterEach(() => server.resetHandlers());

  it('returns a list of systems sorted by name', async () => {
    const listOfSystems = api.getAllSystems();
    await expect(listOfSystems).resolves.toMatchObject({
      systems: [
        expect.objectContaining({
          name: "Our second system"
        }),
        expect.objectContaining({
          name: "System Register"
        })
      ]
    });
  })

  it('returns the system with the specified ID', async () => {
    const system = api.getSystem('1');
    await expect(system).resolves.toMatchObject({
      id: 1,
      name: "System Register"
    });
  })

  describe("update product owner", () => {
    it("sends changed owner to the API", async () => {
      server.use(
        rest.post("/api/systems/345/update-product-owner", (req, res, ctx) => {
          const { product_owner: productOwner } = req.body;
          if (productOwner !== "old owner") {
            console.error("New product owner does not match");
            return;
          }
          if (!req.headers.get("Authorization")?.startsWith("Bearer")) {
            console.error("Authorization header does not contain a bearer token");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateProductOwner(345, {
        productOwner: "old owner"
      });

      await expect(pendingSystem).resolves.toMatchObject(data);
    });

    it("removes the product owner when no value is provided", async () => {
      server.use(
        rest.post("/api/systems/234/update-product-owner", (req, res, ctx) => {
          const { product_owner: productOwner } = req.body;
          if (productOwner !== null) {
            console.error("New product owner does not match");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateProductOwner(234, {
        productOwner: ""
      });

      await expect(pendingSystem).resolves.not.toBeNull();
    });

    it("raises error if validation fails", async () => {
      server.use(
        rest.post("/api/systems/345/update-product-owner", (req, res, ctx) => {
          return res(ctx.status(400), ctx.json({
            errors: {
              productOwner: "invalid owner",
            },
          }));
        })
      );

      const pendingSystem = api.updateProductOwner(345, {
        productOwner: "$"
      });

      await expect(pendingSystem).rejects.toBeInstanceOf(ValidationError);
      await expect(pendingSystem).rejects.toHaveProperty(
        "errors",
        expect.objectContaining({
          productOwner: "invalid owner",
        }));
    });
  });

  describe("update criticality", () => {
    it("sends changed criticality to the API", async () => {
      server.use(
        rest.post("/api/systems/345/update-criticality", (req, res, ctx) => {
          const { criticality: criticality } = req.body;
          if (criticality !== "high") {
            console.error("New criticlaity does not match");
            return;
          }
          if (!req.headers.get("Authorization")?.startsWith("Bearer")) {
            console.error("Authorization header does not contain a bearer token");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateCriticality(345, {
        criticality: "high"
      });

      await expect(pendingSystem).resolves.toMatchObject(data);
    });
  });

  describe("update system name", () => {
    it("sends changed owner to the API", async () => {
      server.use(
        rest.post("/api/systems/345/update-name", (req, res, ctx) => {
          const { name: newName } = req.body;
          if (newName !== "new name") {
            console.error("System name does not match");
            return;
          }
          if (!req.headers.get("Authorization")?.startsWith("Bearer")) {
            console.error("Authorization header does not contain a bearer token");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateSystemName(345, {
        name: "new name"
      });

      await expect(pendingSystem).resolves.toMatchObject(data);
    });

    it("raises error if validation fails", async () => {
      server.use(
        rest.post("/api/systems/345/update-name", (req, res, ctx) => {
          return res(ctx.status(400), ctx.json({
            errors: {
              name: "invalid system name",
            },
          }));
        })
      );

      const pendingSystem = api.updateSystemName(345, {
        name: "$"
      });

      await expect(pendingSystem).rejects.toBeInstanceOf(ValidationError);
      await expect(pendingSystem).rejects.toHaveProperty(
        "errors",
        expect.objectContaining({
          name: "invalid system name",
        }));
    });
  });

  describe("update system description", () => {
    it("sends changed description to the API", async () => {
      server.use(
        rest.post("/api/systems/456/update-system-description", (req, res, ctx) => {
          const { description } = req.body;
          if (description !== "new description") {
            console.error("New description does not match");
            return;
          }
          if (!req.headers.get("Authorization")?.startsWith("Bearer")) {
            console.error("Authorization header does not contain a bearer token");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateSystemDescription(456, {
        description: "new description"
      });

      await expect(pendingSystem).resolves.toMatchObject(data);
    });

    it("removes the description when an empty value is provided", async () => {
      server.use(
        rest.post("/api/systems/234/update-system-description", (req, res, ctx) => {
          const { description } = req.body;
          if (description !== null) {
            console.error("Expected description does not match");
            return;
          }
          return res(ctx.status(200), ctx.json(data));
        })
      );

      const pendingSystem = api.updateSystemDescription(234, {
        description: ""
      });

      await expect(pendingSystem).resolves.not.toBeNull();
    });

    it("raises error if validation fails", async () => {
      server.use(
        rest.post("/api/systems/345/update-system-description", (req, res, ctx) => {
          return res(ctx.status(400), ctx.json({
            errors: {
              description: "invalid description",
            },
          }));
        })
      );

      const pendingSystem = api.updateSystemDescription(345, {
        description: "x"
      });

      await expect(pendingSystem).rejects.toBeInstanceOf(ValidationError);
      await expect(pendingSystem).rejects.toHaveProperty(
        "errors",
        expect.objectContaining({
          description: "invalid description",
        })
      );
    });
  });
});
