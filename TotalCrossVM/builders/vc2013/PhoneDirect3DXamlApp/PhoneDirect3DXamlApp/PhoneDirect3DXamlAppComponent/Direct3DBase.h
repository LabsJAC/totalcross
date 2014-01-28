﻿#pragma once

#include "DirectXHelper.h"
#include "Idummy.h"

#define HAS_TCHAR
#include "tcvm.h"
#define N_LOAD_TASKS 4

struct ProjectionConstantBuffer
{
   DirectX::XMFLOAT4X4 projection;
};

struct VertexPosition
{
   DirectX::XMFLOAT2 pos;
};

struct VertexColor
{
   DirectX::XMFLOAT4 color;
};

struct TextureVertex
{
   DirectX::XMFLOAT2 pos;  // position
   DirectX::XMFLOAT2 tex;  // texture coordinate
};

// Helper class that initializes DirectX APIs for 3D rendering.
ref class Direct3DBase 
{
internal:
	Direct3DBase(PhoneDirect3DXamlAppComponent::Idummy ^_odummy);

	void Initialize(_In_ ID3D11Device1* device);
	void CreateDeviceResources();
	void UpdateDevice(_In_ ID3D11Device1* device, _In_ ID3D11DeviceContext1* context, _In_ ID3D11RenderTargetView* renderTargetView);
	void CreateWindowSizeDependentResources();
	void UpdateForWindowSizeChange(float width, float height);
	void PreRender(); // resets the screen and set it ready to render
	bool RenderTest(); // the screen tester; multiple lines, pixels, a rectangle and a texture
	bool Render();
	void Present(); //XXX must implement this method with setjmp and longjmp!!

   void drawLine(int x1, int y1, int x2, int y2, int color);
   void drawPixels(int *x, int *y, int count, int color);
   void fillRect(int x1, int y1, int x2, int y2, int color);
   void setColor(int color);
   void createTexture();
   void setup();

   bool isLoadCompleted();

   static Direct3DBase ^GetLastInstance();

private:
   int loadCompleted[N_LOAD_TASKS];
   int lastRGB;
   float aa, rr, gg, bb;
   ID3D11Buffer *pBufferRect, *pBufferPixels, *pBufferColor;
   int lastPixelsCount;
   VertexPosition *pixelsVertices;

   // texture
   Microsoft::WRL::ComPtr<ID3D11Buffer> vertexBuffer;
   Microsoft::WRL::ComPtr<ID3D11Buffer> indexBuffer;
   Microsoft::WRL::ComPtr<ID3D11Texture2D> texture;
   Microsoft::WRL::ComPtr<ID3D11ShaderResourceView> textureView;
   Microsoft::WRL::ComPtr<ID3D11SamplerState> texsampler;
   ID3D11DepthStencilState* depthDisabledStencilState;
   ID3D11BlendState* g_pBlendState;


   Microsoft::WRL::ComPtr<ID3D11SamplerState> sampler;
   Microsoft::WRL::ComPtr<ID3D11InputLayout> m_inputLayout, m_inputLayoutT;
   Microsoft::WRL::ComPtr<ID3D11Buffer> m_vertexBuffer;
   Microsoft::WRL::ComPtr<ID3D11Buffer> m_indexBuffer, pixelsIndexBuffer, colorBuffer;
   Microsoft::WRL::ComPtr<ID3D11VertexShader> m_vertexShader, m_vertexShaderT;
   Microsoft::WRL::ComPtr<ID3D11PixelShader> m_pixelShader, m_pixelShaderT;
   Microsoft::WRL::ComPtr<ID3D11Buffer> m_constantBuffer;

   ProjectionConstantBuffer m_constantBufferData;

protected private:
	// Direct3D Objects.
	Microsoft::WRL::ComPtr<ID3D11Device1> m_d3dDevice;
	Microsoft::WRL::ComPtr<ID3D11DeviceContext1> m_d3dContext;
	Microsoft::WRL::ComPtr<ID3D11RenderTargetView> m_renderTargetView;
	Microsoft::WRL::ComPtr<ID3D11DepthStencilView> m_depthStencilView;

	// Cached renderer properties.
	Windows::Foundation::Size m_renderTargetSize;
	Windows::Foundation::Rect m_windowBounds;

	// C# wrapper object
	PhoneDirect3DXamlAppComponent::Idummy ^odummy;

	// TotalCross objects
	Context local_context;
	bool VMStarted;
};