import fetch from 'node-fetch';
import { expect } from 'chai';

describe("Get the Dark Water movie", () => {
  it('Should return HTTP 200 OK', async () => {
    const url = `${process.env.API_BASE_URL}/movie/9009?api_key=${process.env.API_KEY}`;
    const headers = {
      "Content-Type": "application/json",
      "Accept": "application/json",
    }
    console.log(`GET ${url}`);
    let statusCode = await fetch(url, {
      method: 'GET',
      headers: headers
    }).then(async res => {
      return { statusCode: res.status, json: await res.json() }
    }).then(object => {
      console.debug(`Getting the Dark Water movie resolved with ${JSON.stringify(object.json, null, 2)}`);
      return object.statusCode;
    }, error => {
      console.error(`Getting the Dark Water movie rejected with ${JSON.stringify(error)}`);
    })
      .catch(err => {
        console.error(`Getting the Dark Water movie catched with ${JSON.stringify(err)}`);
        reject(err);
      });
    expect(statusCode).to.equal(200);
  });
});
